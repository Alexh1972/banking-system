package org.poo.bank.entity;

public enum CommerciantType {
    FOOD("Food"), CLOTHES("Clothes"), TECH("Tech");

    private String value;
    CommerciantType(String value) {
        this.value = value;
    }

    public static CommerciantType getCommerciantType(String value) {
        for (CommerciantType commerciantType : CommerciantType.values()) {
            if (commerciantType.value.equals(value))
                return commerciantType;
        }

        return CommerciantType.TECH;
    }

    public String getValue() {
        return value;
    }
}
