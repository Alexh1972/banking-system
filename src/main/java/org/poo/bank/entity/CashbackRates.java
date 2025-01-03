package org.poo.bank.entity;

import org.poo.bank.entity.account.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashbackRates {
    private Map<Account, Map<CashbackType, Map<Commerciant, List<Double>>>> cashbackMap;

    public CashbackRates() {
        cashbackMap = new HashMap<>();
    }

    public Map<Commerciant, List<Double>> getCashbackRates(Account account, CashbackType type) {
        Map<CashbackType, Map<Commerciant, List<Double>>> rates = getCashbackRates(account);

        if (rates == null) {
            return null;
        }

        return rates.get(type);
    }

    public List<Double> getCashbackRates(Account account, CashbackType type, Commerciant commerciant) {
        Map<Commerciant, List<Double>> rates = getCashbackRates(account, type);

        if (rates == null) {
            return null;
        }

        return rates.get(commerciant);
    }

    public  Map<CashbackType, Map<Commerciant, List<Double>>> getCashbackRates(Account account) {
        return cashbackMap.get(account);
    }

    public Double getCashbackRate(Account account, CashbackType type) {
        Map<Commerciant, List<Double>> rates = getCashbackRates(account, type);

        if (rates == null) {
            return 0.0;
        }

        Double rate = 0.0;
        for (Commerciant commerciant : rates.keySet()) {
            List<Double> rateValues = rates.get(commerciant);

            if (rateValues == null) {
                continue;
            }

//            for (Double rateValue : rateValues) {
//                rate += rateValue;
//            }

            while (!rateValues.isEmpty()) {
                rate += rateValues.getFirst();
                rateValues.removeFirst();
            }
        }

        if (rate > 1.0) {
            return 1.0;
        }

        return rate;
    }

    public void addCashbackRate(Account account, Double rate, CashbackType type, Commerciant commerciant) {
        Map<CashbackType, Map<Commerciant, List<Double>>> accountRates = getCashbackRates(account);

        if (accountRates == null) {
            accountRates = new HashMap<>();
            cashbackMap.put(account, accountRates);
        }

        Map<Commerciant, List<Double>> commerciantRates = getCashbackRates(account, type);

        if (commerciantRates == null) {
            commerciantRates = new HashMap<>();
            accountRates.put(type, commerciantRates);
        }

        List<Double> rateValues = getCashbackRates(account, type, commerciant);

        if (rateValues == null) {
            rateValues = new ArrayList<>();
            commerciantRates.put(commerciant, rateValues);
        }

        rateValues.add(rate);
    }
}
