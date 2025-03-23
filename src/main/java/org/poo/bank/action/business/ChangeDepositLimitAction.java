package org.poo.bank.action.business;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.user.User;
import org.poo.fileio.CommandInput;

public class ChangeDepositLimitAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());
            User user = bank.getUser(commandInput.getEmail());
            Associates associates = bank.getAssociates(account);
            if (associates == null) {
                throw new RuntimeException("This is not a business account");
            }
            if (!associates.changeDepositLimit(user, commandInput.getAmount())) {
                throw new RuntimeException("You must be owner in order to change deposit limit.");
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
