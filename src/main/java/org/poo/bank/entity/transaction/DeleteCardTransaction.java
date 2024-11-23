package org.poo.bank.entity.transaction;

public class DeleteCardTransaction extends Transaction {
    public DeleteCardTransaction(Integer timestamp) {
        super("Card deleted", timestamp);
    }
}
