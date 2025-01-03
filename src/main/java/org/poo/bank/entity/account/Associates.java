package org.poo.bank.entity.account;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Associates {
    private List<Associate> associates;
    private Double paymentLimit;
    private String currency;

    public Associates() {
        associates = new ArrayList<>();
        paymentLimit = 500.0;
        currency = "RON";
    }
}
