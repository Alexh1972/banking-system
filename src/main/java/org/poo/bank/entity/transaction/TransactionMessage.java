package org.poo.bank.entity.transaction;

import lombok.Getter;

@Getter
public enum TransactionMessage {
    TRANSACTION_MESSAGE_ACCOUNT_CREATED("New account created"),
    TRANSACTION_MESSAGE_CARD_CREATED("New card created"),
    TRANSACTION_MESSAGE_CARD_DELETED("The card has been destroyed"),
    TRANSACTION_MESSAGE_CARD_DELETED_ERROR_OWNER("Wrong owner"),
    TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS("Insufficient funds"),
    TRANSACTION_MESSAGE_ACCOUNT_DELETED("Account deleted"),
    TRANSACTION_MESSAGE_ACCOUNT_DELETE_ERROR("Account couldn't be deleted - see org.poo.transactions for details"),
    TRANSACTION_MESSAGE_CARD_PAYMENT("Card payment"),
    TRANSACTION_MESSAGE_SPLIT_PAYMENT("Split payment of {amount|currency}"),
    TRANSACTION_MESSAGE_SPLIT_PAYMENT_ERROR("Account {IBAN} has insufficient funds for a split payment."),
    TRANSACTION_MESSAGE_SET_CARD_TO_FROZEN("You have reached the minimum amount of funds, the card will be frozen"),
    TRANSACTION_MESSAGE_CARD_STATUS("The card is {status}");

    private final String value;
    TransactionMessage(String value) {
        this.value = value;
    }

    public static TransactionMessage getAccountType(String value) {
        for (TransactionMessage message : TransactionMessage.values()) {
            if (message.getValue().equals(value))
                return message;
        }

        return null;
    }
}
