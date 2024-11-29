package org.poo.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRate {
    private String from;
    private String to;
    private Double rate;
}
