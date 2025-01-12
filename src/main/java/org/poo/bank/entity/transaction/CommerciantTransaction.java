package org.poo.bank.entity.transaction;

import lombok.Getter;

@Getter
public class CommerciantTransaction extends Transaction {
    private final boolean forCommerciant;
    private String commerciant;
    private Double amountSpent;
    private String currencyUsed;
    public CommerciantTransaction(
            final String description,
            final Integer timestamp,
            final String commerciant,
            final boolean forCommerciant,
            final Double amountSpent,
            final String currencyUsed
    ) {
        super(description, timestamp);
        this.forCommerciant = forCommerciant;
        this.commerciant = commerciant;

        this.amountSpent = amountSpent;
        this.currencyUsed = currencyUsed;
    }

    public CommerciantTransaction(
            final String description,
            final Integer timestamp,
            final String commerciant,
            final Double amountSpent,
            final String currencyUsed
    ) {
        super(description, timestamp);
        this.forCommerciant = true;
        this.commerciant = commerciant;

        this.amountSpent = amountSpent;
        this.currencyUsed = currencyUsed;
    }

    public CommerciantTransaction(
            final String description,
            final Integer timestamp,
            final Double amountSpent,
            final String currencyUsed
    ) {
        super(description, timestamp);
        this.forCommerciant = false;

        this.amountSpent = amountSpent;
        this.currencyUsed = currencyUsed;
    }
}
