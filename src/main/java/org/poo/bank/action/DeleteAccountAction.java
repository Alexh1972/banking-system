package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.fileio.CommandInput;

public class DeleteAccountAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null)
                throw new RuntimeException("User not found");

            User user = bank.getUser(account);

            if (user == null)
                throw new RuntimeException("User not found");

            if (account.getBalance() == 0)
                bank.deleteAccount(user, account);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
