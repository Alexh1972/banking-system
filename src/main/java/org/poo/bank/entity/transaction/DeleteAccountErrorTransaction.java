package org.poo.bank.entity.transaction;

public class DeleteAccountErrorTransaction extends Transaction {
    public DeleteAccountErrorTransaction(final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_ACCOUNT_DELETE_ERROR
                .getValue(),
                timestamp);
    }
}
