package org.poo.bank.entity.transaction;

import lombok.Getter;

@Getter
public enum TransferType {
    TRANSFER_TYPE_SENT("sent"),
    TRANSFER_TYPE_RECEIVED("received"),
    TRANSFER_TYPE_INSUFFICIENT_FUNDS("insufficient funds"),
    TRANSFER_TYPE_FROZEN_CARD("frozen card"),
    TRANSFER_TYPE_SUCCESSFUL("successful");
    private final String value;

    TransferType(String value) {
        this.value = value;
    }

    public static TransferType getAccountType(String value) {
        for (TransferType message : TransferType.values()) {
            if (message.getValue().equals(value))
                return message;
        }

        return null;
    }
}
