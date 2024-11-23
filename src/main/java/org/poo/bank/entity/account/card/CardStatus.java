package org.poo.bank.entity.account.card;

import lombok.Getter;

@Getter
public enum CardStatus {
    CARD_STATUS_ACTIVE("active"), CARD_STATUS_FROZEN("frozen"), CARD_STATUS("warning");

    private final String value;

    CardStatus(String value) {
        this.value = value;
    }

    public static CardStatus getCardStatus(String value) {
        for (CardStatus status : CardStatus.values()) {
            if (status.getValue().equals(value))
                return status;
        }

        return CARD_STATUS_ACTIVE;
    }
}
