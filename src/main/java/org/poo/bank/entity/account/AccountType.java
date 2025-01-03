package org.poo.bank.entity.account;

import lombok.Getter;

@Getter
public enum AccountType {
    ACCOUNT_TYPE_SAVINGS("savings"),
    ACCOUNT_TYPE_CLASSIC("classic"),
    ACCOUNT_TYPE_BUSINESS("business");

    private final String value;

    AccountType(final String value) {
        this.value = value;
    }

    /**
     * Get the account type by a given string.
     * @param value The string.
     * @return The account type.
     */
    public static AccountType getAccountType(final String value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.getValue().equals(value)) {
                return accountType;
            }
        }

        return ACCOUNT_TYPE_CLASSIC;
    }
}
