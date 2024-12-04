package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.InsufficientFundsTransaction;
import org.poo.bank.entity.transaction.SendMoneyTransaction;
import org.poo.bank.entity.transaction.TransferType;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class SendMoneyAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account sender = bank.getAccount(commandInput.getAccount());
            User senderUser = bank.getUser(sender);

            Account receiver = bank.getAlias(commandInput.getReceiver());
            User receiverUser = bank.getUser(receiver);

            if (sender == null || receiver == null) {
                return null;
            }

            if (commandInput.getDescription().equals("Repayment for a personal loan")) {
                int a = 1;
            }
            Double amount = bank.getAmount(commandInput.getAmount(), sender.getCurrency(), receiver.getCurrency());
            if (sender.subtractBalance(commandInput.getAmount())) {
                receiver.addBalance(amount);

                if (receiver.getIBAN().equals(sender.getIBAN()))
                    TransactionNotifier.notify(new SendMoneyTransaction(
                                    commandInput.getDescription(),
                                    commandInput.getTimestamp(),
                                    amount,
                                    receiver.getCurrency(),
                                    receiver.getIBAN(),
                                    sender.getIBAN(),
                                    TransferType.TRANSFER_TYPE_RECEIVED),
                            receiverUser,
                            receiver);
                TransactionNotifier.notify(new SendMoneyTransaction(
                                commandInput.getDescription(),
                                commandInput.getTimestamp(),
                                commandInput.getAmount(),
                                sender.getCurrency(),
                                receiver.getIBAN(),
                                sender.getIBAN(),
                                TransferType.TRANSFER_TYPE_SENT),
                        senderUser,
                        sender);
                if (!receiver.getIBAN().equals(sender.getIBAN()))
                    TransactionNotifier.notify(new SendMoneyTransaction(
                                    commandInput.getDescription(),
                                    commandInput.getTimestamp(),
                                    amount,
                                    receiver.getCurrency(),
                                    receiver.getIBAN(),
                                    sender.getIBAN(),
                                    TransferType.TRANSFER_TYPE_RECEIVED),
                            receiverUser,
                            receiver);
            } else {
                TransactionNotifier.notify(new InsufficientFundsTransaction(commandInput.getTimestamp()), senderUser, sender);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
