package org.poo.bank;

import org.poo.bank.Bank;

public class BankSingleton {
    private static final Bank bank = new Bank();

    public static Bank getInstance() {
        return bank;
    }
}
