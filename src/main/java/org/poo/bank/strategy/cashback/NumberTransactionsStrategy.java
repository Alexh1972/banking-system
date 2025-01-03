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
        List<Transaction> transactions = getTransactions(account, commerciant);
        CashbackType cashbackType = CashbackType.getCashbackType(commerciant.getType().getValue());

        List<Double> rates = bank.getCashbackRates().getCashbackRates(account, cashbackType, commerciant);

        double rate = 0.0;
        if (transactions.size() >= 10) {
            rate = 10.0 / 100;
        } else if (transactions.size() >= 5) {
            rate = 5.0 / 100;
        } else if (transactions.size() >= 2) {
            rate = 2.0 / 100;
        }

        if (rate != 0.0) {
            if (rates == null || rates.contains(rate)) {
                bank.getCashbackRates().addCashbackRate(account, rate, cashbackType, commerciant);
            }
        }
    }
}
