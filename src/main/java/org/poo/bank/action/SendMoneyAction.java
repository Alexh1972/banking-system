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

            Account receiver = bank.getAccount(senderUser.getEmail(), commandInput.getReceiver());
            User receiverUser = bank.getUser(receiver);

            if (sender == null || receiver == null) {
                throw new RuntimeException("User not found");
            }

            Double amount = bank.getAmount(commandInput.getAmount(), sender.getCurrency(), receiver.getCurrency());
            if (sender.subtractBalance(commandInput.getAmount())) {
                receiver.addBalance(amount);
                TransactionNotifier.notify(new SendMoneyTransaction(
                                commandInput.getDescription(),
                                commandInput.getTimestamp(),
                                commandInput.getAmount(),
                                sender.getCurrency(),
                                receiver.getIBAN(),
                                sender.getIBAN(),
                                TransferType.TRANSFER_TYPE_SENT),
                        senderUser);
                TransactionNotifier.notify(new SendMoneyTransaction(
                                commandInput.getDescription(),
                                commandInput.getTimestamp(),
                                amount,
                                receiver.getCurrency(),
                                receiver.getIBAN(),
                                sender.getIBAN(),
                                TransferType.TRANSFER_TYPE_RECEIVED),
                        receiverUser);
            } else {
                TransactionNotifier.notify(new InsufficientFundsTransaction(commandInput.getTimestamp()), senderUser);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
