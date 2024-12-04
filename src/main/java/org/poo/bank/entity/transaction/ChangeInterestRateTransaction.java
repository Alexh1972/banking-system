package org.poo.bank.entity.transaction;

public class ChangeInterestRateTransaction extends Transaction {
    public ChangeInterestRateTransaction(Double amount, Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CHANGE_INTEREST_RATE
                        .getValue()
                        .replace(
                                "{amount}",
                                amount.toString()),
                timestamp);
    }
}
