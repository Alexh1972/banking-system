package org.poo.bank.entity.transaction;

public class BaseTransaction extends Transaction {
    public BaseTransaction(
            final String description,
            final Integer timestamp
    ) {
        super(description, timestamp);
    }
}
