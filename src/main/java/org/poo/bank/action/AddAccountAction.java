package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.transaction.CreateAccountTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.notification.TransactionNotifier;
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
                    .minimumBalance(0.0)
                    .interestRate(commandInput.getInterestRate())
                    .build();

            bank.addAccount(account, user);
            TransactionNotifier.notify(new CreateAccountTransaction(commandInput.getTimestamp()), user, account);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
