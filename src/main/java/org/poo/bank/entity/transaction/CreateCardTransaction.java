package org.poo.bank.entity.transaction;

public class CreateCardTransaction extends Transaction {
    public CreateCardTransaction(Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_CREATED.getValue(), timestamp);
    }
    public CreateCardTransaction(TransactionMessage message, Integer timestamp) {
        super(message.getValue(), timestamp);
    }
}
