package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.user.User;
import org.poo.fileio.CommandInput;

public class ChangeDepositLimitAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());
            User user = bank.getUser(account);
            Associates associates = bank.getAssociates(account);

            if (associates.changeDepositLimit(user, commandInput.getAmount())) {

            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
