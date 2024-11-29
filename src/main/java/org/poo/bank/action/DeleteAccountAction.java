package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.fileio.CommandInput;

public class DeleteAccountAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null)
                throw new RuntimeException("User not found");

            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null)
                throw new RuntimeException("User not found");

            User user = bank.getUser(account);

            if (user == null || !user.equals(bank.getUser(commandInput.getEmail())))
                throw new RuntimeException("User not found");

            ObjectNode resultNode = getMapper().createObjectNode();
            ObjectNode objectNode = getMapper().createObjectNode();
            if (account.getBalance() == 0) {
                bank.deleteAccount(user, account);

                resultNode.put("success", TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_DELETED.getValue());
            } else {
                resultNode.put("error", TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_DELETE_ERROR.getValue());
            }

            resultNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("output", resultNode);
            return objectNode;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
