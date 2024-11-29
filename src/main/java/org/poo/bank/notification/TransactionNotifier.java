package org.poo.bank.notification;

import lombok.Data;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionNotifier {
    private List<User> notifiedUsers = new ArrayList<>();
    private List<Account> notifiedAccount = new ArrayList<>();
    public void addListener(User user, Account account) {
        notifiedUsers.add(user);
        notifiedAccount.add(account);
    }

    public void removeListener(User user, Account account) {
        notifiedUsers.remove(user);
        notifiedAccount.remove(account);
    }

    public static void notify(Transaction transaction, User user, Account account) {
        if (user != null) {
            user.transactionUpdate(transaction);
        }

        if (account != null) {
            account.transactionUpdate(transaction);
        }
    }
    public void notifyUsers(Transaction transaction) {
        for (User user : notifiedUsers) {
            user.transactionUpdate(transaction);
        }

        for (Account account : notifiedAccount) {
            account.transactionUpdate(transaction);
        }
    }
}
