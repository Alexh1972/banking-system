package org.poo.bank.entity.transaction.payment;

import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;

public class InsufficientFundsTransaction extends Transaction {
    public InsufficientFundsTransaction(final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS.getValue(), timestamp);
    }
}
