package org.poo.bank.entity;

import lombok.Getter;

@Getter
public enum CommerciantType {
    FOOD("Food"),
    CLOTHES("Clothes"),
    TECH("Tech");

    private String value;
    CommerciantType(final String value) {
        this.value = value;
    }

    /**
     * Gets the commerciant type by its name.
     * @param value The name.
     * @return The commerciant type.
     */
    public static CommerciantType getCommerciantType(final String value) {
        for (CommerciantType commerciantType : CommerciantType.values()) {
            if (commerciantType.value.equals(value)) {
                return commerciantType;
            }
        }

        return CommerciantType.TECH;
    }
}
