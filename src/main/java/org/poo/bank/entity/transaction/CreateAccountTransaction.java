package org.poo.bank.entity.transaction;

import lombok.Data;

public class CreateAccountTransaction extends Transaction {
    public CreateAccountTransaction(Integer timestamp) {
        super("New account created", timestamp);
    }
}
