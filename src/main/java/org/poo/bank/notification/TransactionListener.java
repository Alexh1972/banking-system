package org.poo.bank.notification;

import org.poo.bank.entity.transaction.Transaction;

public interface TransactionListener {
    /**
     * Add a notification to the listener.
     * @param transaction The transaction.
     */
    void transactionUpdate(Transaction transaction);
}
