package org.poo.bank.action.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.transaction.account.AddInterestTransaction;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class AddInterestAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                throw new RuntimeException("Account not found");
            }

            if (!account.getAccountType().equals(AccountType.ACCOUNT_TYPE_SAVINGS)) {
                throw new RuntimeException("This is not a savings account");
            }

            User user = bank.getUser(account);
            TransactionNotifier.notify(
                    new AddInterestTransaction(
                            account.getInterestAmount(),
                            account.getCurrency(),
                            commandInput.getTimestamp()
                    ),
                    user,
                    account
            );
            account.addInterest();
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
