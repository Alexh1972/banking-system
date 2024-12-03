package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.SplitPaymentErrorTransaction;
import org.poo.bank.entity.transaction.SplitPaymentTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        TransactionNotifier notifier = new TransactionNotifier();
        List<String> accounts = commandInput.getAccounts();
        List<Account> accountsList = new ArrayList<>();
        boolean isPaymentValid = true;
        String errorIBAN = null;
        int numberPayers = accounts.size();

        for (String accountIBAN : accounts) {
            Account account = bank.getAccount(accountIBAN);
            Double amount = bank.getAmount(commandInput.getAmount(),
                    account.getCurrency(),
                    commandInput.getCurrency()) / numberPayers; // to - from pe invers

            if (!account.canPay(amount)) {
                isPaymentValid = false;
                errorIBAN = accountIBAN;
            }

            accountsList.add(account);
            notifier.addListener(bank.getUser(account), account);
        }

        if (isPaymentValid) {
            for (Account account : accountsList) {
                Double amount = bank.getAmount(commandInput.getAmount(),
                        account.getCurrency(),
                        commandInput.getCurrency()) / numberPayers; // to - from pe invers

                account.subtractBalance(amount);
            }

            Transaction transaction = new SplitPaymentTransaction(
                    commandInput.getAmount(),
                    commandInput.getCurrency(),
                    numberPayers,
                    accounts,
                    commandInput.getTimestamp());
            notifier.notifyUsers(transaction);
        } else {
            Transaction transaction = new SplitPaymentErrorTransaction(
                    commandInput.getAmount(),
                    commandInput.getCurrency(),
                    numberPayers,
                    accounts,
                    errorIBAN,
                    commandInput.getTimestamp());
            notifier.notifyUsers(transaction);
        }

        return null;
    }
}
