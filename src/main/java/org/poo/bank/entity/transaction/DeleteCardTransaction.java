package org.poo.bank.entity.transaction;

public class DeleteCardTransaction extends Transaction {
    public DeleteCardTransaction(Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_DELETED.getValue(), timestamp);
    }
}
