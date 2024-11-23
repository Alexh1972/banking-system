package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.fileio.CommandInput;

public class AddFundAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        Account account = bank.getAccountIBANMap().get(commandInput.getAccount());

        if (account == null)
            throw new RuntimeException("Account with IBAN '" + commandInput.getEmail() + "' not found");

        Double balance = account.getBalance();
        account.setBalance(balance + commandInput.getAmount());
        return null;
    }
}
