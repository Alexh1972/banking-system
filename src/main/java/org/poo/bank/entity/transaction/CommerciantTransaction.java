package org.poo.bank.entity.transaction;

import lombok.Getter;

@Getter
public class CommerciantTransaction extends Transaction {
    private final boolean forCommerciant;
    private String commerciant;
    private Double amountSpent;
    private String currencyUsed;
    public CommerciantTransaction(String description, Integer timestamp, String commerciant, boolean forCommerciant, Double amountSpent, String currencyUsed) {
        super(description, timestamp);
        this.forCommerciant = forCommerciant;
        this.commerciant = commerciant;

        this.amountSpent = amountSpent;
        this.currencyUsed = currencyUsed;
    }

    public CommerciantTransaction(String description, Integer timestamp, String commerciant, Double amountSpent, String currencyUsed) {
        super(description, timestamp);
        this.forCommerciant = true;
        this.commerciant = commerciant;

        this.amountSpent = amountSpent;
        this.currencyUsed = currencyUsed;
    }

    public CommerciantTransaction(String description, Integer timestamp, Double amountSpent, String currencyUsed) {
        super(description, timestamp);
        this.forCommerciant = false;

        this.amountSpent = amountSpent;
        this.currencyUsed = currencyUsed;
    }
}
