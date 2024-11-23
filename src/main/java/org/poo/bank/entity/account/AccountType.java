package org.poo.bank.entity.account;

import lombok.Getter;

@Getter
public enum AccountType {
    ACCOUNT_TYPE_SAVINGS("savings"), ACCOUNT_TYPE_CLASSIC("classic");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public static AccountType getAccountType(String value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.getValue().equals(value))
                return accountType;
        }

        return ACCOUNT_TYPE_CLASSIC;
    }
}
