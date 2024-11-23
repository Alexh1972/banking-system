package org.poo.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeValue {
    private String to;
    private String from;
    private Double rate;
}
