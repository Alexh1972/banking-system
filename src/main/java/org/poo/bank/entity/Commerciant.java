package org.poo.bank.entity;

import lombok.Data;
import org.poo.bank.strategy.cashback.CashbackStrategy;

@Data
public class Commerciant {
    private String name;
    private Integer id;
    private String accountIban;
    private CommerciantType type;
    private CashbackStrategy cashbackStrategy;

    public Commerciant(
            final String name,
            final Integer id,
            final String accountIban,
            final String type,
            final String cashbackStrategy
    ) {
        this.name = name;
        this.id = id;
        this.accountIban = accountIban;
        this.type = CommerciantType.getCommerciantType(type);
        this.cashbackStrategy = CashbackStrategy.getCashbackStrategy(cashbackStrategy);
    }
}
