package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.InsufficientFundsTransaction;
import org.poo.bank.entity.transaction.SendMoneyTransaction;
import org.poo.bank.entity.transaction.TransferType;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class SendMoneyAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account sender = bank.getAccount(commandInput.getAccount());
            User senderUser = bank.getUser(sender);

            Account receiver = bank.getAlias(commandInput.getReceiver());
            User receiverUser = bank.getUser(receiver);

            if (sender == null || receiver == null) {
                throw new RuntimeException("User not found");
            }

            Double amount = bank.getAmount(commandInput.getAmount(),
                    sender.getCurrency(),
                    receiver.getCurrency());

            Double amountSent = commandInput.getAmount();
            amountSent += sender.getServicePlan().getCommission(amountSent, sender.getCurrency());
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
