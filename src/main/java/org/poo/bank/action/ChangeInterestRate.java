package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.fileio.CommandInput;

public class ChangeInterestRate extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null)
                throw new RuntimeException("Account not found");

            if (!account.getAccountType().equals(AccountType.ACCOUNT_TYPE_SAVINGS))
                throw new RuntimeException("This is not a savings account");

            account.setInterestRate(commandInput.getInterestRate());

            return null;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
