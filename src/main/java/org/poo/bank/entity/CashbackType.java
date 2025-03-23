package org.poo.bank.entity;

import lombok.Getter;

@Getter
public enum CashbackType {
    FOR_FOOD("Food"),
    FOR_CLOTHES("Clothes"),
    FOR_TECH("Tech"), FOR_SPENDING_THRESHOLD_COMMERCIANT("spendingThreshold");

    private String value;
    CashbackType(final String value) {
        this.value = value;
    }

    /**
     * Gets the cashback type by its name.
     * @param value The name.
     * @return The cashback type.
     */
    public static CashbackType getCashbackType(final String value) {
        for (CashbackType cashbackType : CashbackType.values()) {
            if (cashbackType.value.equals(value)) {
                return cashbackType;
            }
        }

        return FOR_SPENDING_THRESHOLD_COMMERCIANT;
    }
}
