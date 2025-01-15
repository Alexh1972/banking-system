package org.poo.bank.entity.transaction.account;

import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;

public class CreateAccountTransaction extends Transaction {
    public CreateAccountTransaction(final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_CREATED
                .getValue(),
                timestamp);
    }
}
