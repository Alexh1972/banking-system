package org.poo.bank.entity.transaction;

import lombok.Getter;

@Getter
public enum TransferType {
    TRANSFER_TYPE_SENT("sent"),
    TRANSFER_TYPE_RECEIVED("received");
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
