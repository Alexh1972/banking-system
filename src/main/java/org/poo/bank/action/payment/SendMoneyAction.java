package org.poo.bank.action.payment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.ServicePlan;
import org.poo.bank.entity.transaction.account.UpgradePlanTransaction;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.payment.InsufficientFundsTransaction;
import org.poo.bank.entity.transaction.payment.SendMoneyTransaction;
import org.poo.bank.entity.transaction.TransferType;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class SendMoneyAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account sender = bank.getAlias(commandInput.getAccount());
            User senderUser = bank.getUser(sender);

            Account receiver = bank.getAlias(commandInput.getReceiver());
            User receiverUser = bank.getUser(receiver);
            if (sender == null || receiver == null) {
                if (sender != null) {
                    Commerciant commerciant = bank.getCommerciantByIban(commandInput.getReceiver());

                    if (commerciant == null) {
                        throw new RuntimeException("User not found");
                    }
                    Double amount = commandInput.getAmount();

                    Double commission = senderUser
                            .getServicePlan()
                            .getCommission(
                                    commandInput.getAmount(),
                                    sender.getCurrency()
                            );

                    Double cashbackAmount =
                            commerciant.getCashbackStrategy().getCashbackRate(
                                    sender,
                                    commerciant,
                                    amount
                            )
                            * amount;

                    Double amountSent = commandInput.getAmount() + commission - cashbackAmount;

                    if (sender.subtractBalance(amountSent)) {
                        commerciant.getCashbackStrategy().updateCashback(sender, commerciant);
                        TransactionNotifier.notify(new SendMoneyTransaction(
                                        commandInput.getDescription(),
                                        commandInput.getTimestamp(),
                                        commandInput.getAmount(),
                                        sender.getCurrency(),
                                        commerciant.getAccountIban(),
                                        sender.getIban(),
                                        TransferType.TRANSFER_TYPE_SENT,
                                        commerciant.getName(),
                                        true),
                                senderUser,
                                sender);
                    } else {
                        TransactionNotifier.notify(
                                new InsufficientFundsTransaction(
                                        commandInput.getTimestamp()),
                                senderUser,
                                sender);
                    }
                    return null;
                }
                throw new RuntimeException("User not found");
            }

            Double amount = bank.getAmount(commandInput.getAmount(),
                    sender.getCurrency(),
                    receiver.getCurrency());

            Double commission =
                    senderUser.getServicePlan().getCommission(
                            commandInput.getAmount(),
                            sender.getCurrency()
                    );

            Double amountSent = commandInput.getAmount() + commission;

            if (sender.subtractBalance(amountSent)) {
                receiver.addBalance(amount);

                if (receiver.getIban().equals(sender.getIban())) {
                    TransactionNotifier.notify(new SendMoneyTransaction(
                                    commandInput.getDescription(),
                                    commandInput.getTimestamp(),
                                    amount,
                                    receiver.getCurrency(),
                                    receiver.getIban(),
                                    sender.getIban(),
                                    TransferType.TRANSFER_TYPE_RECEIVED),
                            receiverUser,
                            receiver);
                }

                TransactionNotifier.notify(new SendMoneyTransaction(
                                commandInput.getDescription(),
                                commandInput.getTimestamp(),
                                commandInput.getAmount(),
                                sender.getCurrency(),
                                receiver.getIban(),
                                sender.getIban(),
                                TransferType.TRANSFER_TYPE_SENT),
                        senderUser,
                        sender);

                if (!receiver.getIban().equals(sender.getIban())) {
                    TransactionNotifier.notify(new SendMoneyTransaction(
                                    commandInput.getDescription(),
                                    commandInput.getTimestamp(),
                                    amount,
                                    receiver.getCurrency(),
                                    receiver.getIban(),
                                    sender.getIban(),
                                    TransferType.TRANSFER_TYPE_RECEIVED),
                            receiverUser,
                            receiver);
                }

                if (sender.canUpgradePlan(amountSent)) {
                    senderUser.setServicePlan(ServicePlan.GOLD);
                    TransactionNotifier.notify(new UpgradePlanTransaction(
                                    ServicePlan.GOLD.getName(),
                                    sender.getIban(),
                                    commandInput.getTimestamp()),
                            senderUser,
                            sender);
                }
            } else {
                TransactionNotifier.notify(
                        new InsufficientFundsTransaction(
                                commandInput.getTimestamp()),
                        senderUser,
                        sender);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
