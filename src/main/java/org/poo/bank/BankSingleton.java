package org.poo.bank;

public class BankSingleton {
    private static final Bank BANK = new Bank();
    protected BankSingleton() {

    }
    public static Bank getInstance() {
        return BANK;
    }
}
