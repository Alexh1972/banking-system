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

    /**
     * Adds a listener to a notification.
     * @param user The listener's user.
     * @param account THe listener's account.
     */
    public final void addListener(final User user,
                                  final Account account) {
        notifiedUsers.add(user);
        notifiedAccount.add(account);
    }

    /**
     * Removes a listener.
     * @param user The listener's user.
     * @param account The listener's account.
     */
    public final void removeListener(final User user,
                                     final Account account) {
        notifiedUsers.remove(user);
        notifiedAccount.remove(account);
    }

    /**
     * Notifies a listener with a transaction.
     * @param transaction The transaction.
     * @param user The listener's user.
     * @param account The listener's account.
     */
    public static void notify(final Transaction transaction,
                              final User user,
                              final Account account) {
        if (user != null) {
            user.transactionUpdate(transaction);
        }

        if (account != null) {
            account.transactionUpdate(transaction);
        }
    }

    /**
     * Notifies all listeners with a transaction.
     * @param transaction The transaction.
     */
    public final void notifyUsers(final Transaction transaction) {
        for (User user : notifiedUsers) {
            user.transactionUpdate(transaction);
        }

        for (Account account : notifiedAccount) {
            account.transactionUpdate(transaction);
        }
    }
}
