package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddAccountAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            User user = bank.getUser(commandInput.getEmail());

            if (user == null)
                throw new RuntimeException("User not found");

            Account account = Account.builder()
                    .currency(commandInput.getCurrency())
                    .accountType(AccountType.getAccountType(commandInput.getAccountType()))
                    .interestRate(commandInput.getInterestRate())
                    .IBAN(Utils.generateIBAN())
                    .balance(0.0)
                    .cards(new ArrayList<>())
                    .build();

            bank.addAccount(account, user);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
