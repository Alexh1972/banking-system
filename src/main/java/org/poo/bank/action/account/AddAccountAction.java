package org.poo.bank.action.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.transaction.account.CreateAccountTransaction;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class AddAccountAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            User user = bank.getUser(commandInput.getEmail());

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            Account account = Account.builder()
                    .currency(commandInput.getCurrency())
                    .accountType(
                            AccountType
                                    .getAccountType(
                                            commandInput
                                                    .getAccountType()))
                    .interestRate(commandInput.getInterestRate())
                    .iban(Utils.generateIBAN())
                    .balance(0.0)
                    .minimumBalance(0.0)
                    .interestRate(commandInput.getInterestRate())
                    .build();

            if (account.getAccountType().equals(AccountType.ACCOUNT_TYPE_BUSINESS)) {
                bank.addAssociate(account, user, Associate.AssociateType.OWNER.getName());
            }

            bank.addAccount(account, user);
            TransactionNotifier.notify(
                    new CreateAccountTransaction(
                            commandInput.getTimestamp()),
                    user,
                    account);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
