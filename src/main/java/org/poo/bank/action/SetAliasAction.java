package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetAliasAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null)
                throw new RuntimeException("User not found");

            bank.setAlias(commandInput.getEmail(), commandInput.getAlias(), commandInput.getAccount());
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
