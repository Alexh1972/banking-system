package org.poo.bank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRate {
    private String to;
    private String from;
    private Double rate;
}
