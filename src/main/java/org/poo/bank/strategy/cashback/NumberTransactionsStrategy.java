package org.poo.bank.strategy.cashback;

import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.CashbackType;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.CommerciantType;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberTransactionsStrategy extends CashbackStrategy {
    @Override
    public Double getCashbackRate(Account account, Commerciant commerciant, Double amount) {
        Bank bank = BankSingleton.getInstance();
        return bank.getCashbackRates().getCashbackRate(account,
                CashbackType.getCashbackType(commerciant.getType().getValue()));
    }

    @Override
    public void updateCashback(Account account, Commerciant commerciant) {
        Bank bank = BankSingleton.getInstance();
        bank.getCashbackRates().deleteCashbackRate(account, CashbackType.getCashbackType(commerciant.getType().getValue()));
        List<Transaction> transactions = getTransactions(account, commerciant);
        CashbackType cashbackType = null;

        List<Double> rates = bank.getCashbackRates().getCashbackRates(account, cashbackType, commerciant);

        int size = transactions.size() + 1;
        double rate = 0.0;
        if (size >= 10) {
            rate = 10.0 / 100;
            cashbackType = CashbackType.FOR_TECH;
        } else if (size >= 5) {
            rate = 5.0 / 100;
            cashbackType = CashbackType.FOR_CLOTHES;
        } else if (size >= 2) {
            rate = 2.0 / 100;
            cashbackType = CashbackType.FOR_FOOD;
        }
        if (rate != 0.0) {
            if (rates == null || !rates.contains(rate)) {
                bank.getCashbackRates().addCashbackRate(account, rate, cashbackType, commerciant);
            }
        }
    }
}
