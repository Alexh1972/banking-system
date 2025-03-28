package org.poo.bank.action.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.account.DeleteAccountErrorTransaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class DeleteAccountAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null) {
                throw new RuntimeException("User not found");
            }

            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                throw new RuntimeException("User not found");
            }

            User user = bank.getUser(account);

            if (user == null || !user.equals(bank.getUser(commandInput.getEmail()))) {
                throw new RuntimeException("User not found");
            }

            ObjectNode resultNode = getMapper().createObjectNode();
            ObjectNode objectNode = getMapper().createObjectNode();
            if (account.getBalance() == 0) {
                bank.deleteAccount(user, account);

                resultNode.put("success",
                        TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_DELETED
                                .getValue());
            } else {
                resultNode.put("error",
                        TransactionMessage
                                .TRANSACTION_MESSAGE_ACCOUNT_DELETE_ERROR_OUT
                                .getValue());
                TransactionNotifier.notify(
                        new DeleteAccountErrorTransaction(
                                commandInput.getTimestamp()),
                        user,
                        account);
            }

            resultNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("output", resultNode);
            return objectNode;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
