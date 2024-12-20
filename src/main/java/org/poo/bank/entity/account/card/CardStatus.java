package org.poo.bank.entity.account.card;

import lombok.Getter;

@Getter
public enum CardStatus {
    CARD_STATUS_ACTIVE("active"), CARD_STATUS_FROZEN("frozen"), CARD_STATUS_WARNING("warning");

    private final String value;

    CardStatus(final String value) {
        this.value = value;
    }

    /**
     * Gets the card status by a given string.
     * @param value The string.
     * @return The card status.
     */
    public static CardStatus getCardStatus(final String value) {
        for (CardStatus status : CardStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        return CARD_STATUS_ACTIVE;
    }
}
