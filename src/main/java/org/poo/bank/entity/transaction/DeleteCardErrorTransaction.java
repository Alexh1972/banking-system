package org.poo.bank.entity.transaction;

public class DeleteCardErrorTransaction extends Transaction {
    public DeleteCardErrorTransaction(Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_DELETE_ERROR.getValue(), timestamp);
    }
}
