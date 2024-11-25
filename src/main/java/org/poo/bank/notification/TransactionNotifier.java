package org.poo.bank.notification;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.User;
import org.poo.bank.entity.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionNotifier {
    private List<User> notifiedUsers = new ArrayList<>();
    public void addListener(User user) {
        notifiedUsers.add(user);
    }

    public void removeListener(User user) {
        notifiedUsers.remove(user);
    }

    public static void notify(Transaction transaction, User user) {
        user.transactionUpdate(transaction);
    }
    public void notifyUsers(Transaction transaction) {
        for (User user : notifiedUsers) {
            user.transactionUpdate(transaction);
        }
    }
}
