package org.poo.bank.entity.transaction.account;

import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;

public class ChangeInterestRateTransaction extends Transaction {
    public ChangeInterestRateTransaction(final Double amount,
                                         final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CHANGE_INTEREST_RATE
                        .getValue()
                        .replace(
                                "{amount}",
                                amount.toString()),
                timestamp);
    }
}
