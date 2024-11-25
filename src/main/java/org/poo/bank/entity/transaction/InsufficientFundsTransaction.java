package org.poo.bank.entity.transaction;

public class InsufficientFundsTransaction extends Transaction {
    public InsufficientFundsTransaction(Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS.getValue(), timestamp);
    }
}
