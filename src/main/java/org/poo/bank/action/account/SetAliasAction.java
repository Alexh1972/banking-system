package org.poo.bank.action.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.fileio.CommandInput;

public class SetAliasAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null) {
                throw new RuntimeException("User not found");
            }

            bank.setAlias(commandInput.getAlias(), commandInput.getAccount());
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
