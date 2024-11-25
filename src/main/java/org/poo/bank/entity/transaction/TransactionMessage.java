package org.poo.bank.entity.transaction;

import lombok.Getter;

@Getter
public enum TransactionMessage {
    TRANSACTION_MESSAGE_ACCOUNT_CREATED("New account created"),
    TRANSACTION_MESSAGE_CARD_CREATED("New card created"),
    TRANSACTION_MESSAGE_CARD_DELETED("Card deleted"),
    TRANSACTION_MESSAGE_CARD_DELETED_ERROR_OWNER("Wrong owner"),
    TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS("Insufficient funds");

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
