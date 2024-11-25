package org.poo.bank.entity.transaction;

import lombok.Data;

public class CreateAccountTransaction extends Transaction {
    public CreateAccountTransaction(Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_CREATED.getValue(), timestamp);
    }
}
