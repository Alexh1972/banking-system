package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.fileio.CommandInput;

public class AddAccountAssociateAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());
            bank.addAssociate(account, commandInput.getEmail(), commandInput.getRole());
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
