package org.poo.bank.entity.transaction;

public class CreateCardTransaction extends Transaction {
    public CreateCardTransaction(Integer timestamp) {
        super("New card created", timestamp);
    }
}
