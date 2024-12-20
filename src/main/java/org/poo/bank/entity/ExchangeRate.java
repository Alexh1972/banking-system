package org.poo.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeRate {
    private String from;
    private String to;
    private BigDecimal rate;
}
