package org.poo.bank.notification;

import org.poo.bank.entity.transaction.Transaction;

public interface TransactionListener {
    void transactionUpdate(Transaction transaction);
}
