package org.poo.bank.entity.transaction;

public class CreateAccountTransaction extends Transaction {
    public CreateAccountTransaction(final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_CREATED
                .getValue(),
                timestamp);
    }
}
