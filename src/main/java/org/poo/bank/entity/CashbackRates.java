package org.poo.bank.entity;

import org.poo.bank.entity.account.Account;

import java.util.*;

public class CashbackRates {
    private final Map<Account, Map<CashbackType, Map<Commerciant, List<Double>>>> cashbackMap;
    private final Map<Account, Set<CashbackType>> usedCashbacksMap;

    public CashbackRates() {
        cashbackMap = new HashMap<>();
        usedCashbacksMap = new HashMap<>();
    }

    /**
     * Gets the rates given by each commerciant
     * for the account.
     * @param account The account.
     * @param type The type of cashback.
     * @return The rates.
     */
    public final Map<Commerciant, List<Double>>
    getCashbackRates(
            final Account account,
            final CashbackType type
    ) {
        Map<CashbackType, Map<Commerciant, List<Double>>> rates = getCashbackRates(account);

        if (rates == null) {
            return null;
        }

        return rates.get(type);
    }

    /**
     * Gets the rates given by a commerciant
     * for the account.
     * @param account The account.
     * @param type The type of cashback.
     * @param commerciant The commerciant.
     * @return The rates.
     */
    public final List<Double>
    getCashbackRates(
            final Account account,
            final CashbackType type,
            final Commerciant commerciant
    ) {
        Map<Commerciant, List<Double>> rates = getCashbackRates(account, type);

        if (rates == null) {
            return null;
        }

        return rates.get(commerciant);
    }

    /**
     * Gets all the rates for an account.
     * @param account The account.
     * @return The rates.
     */
    public final Map<CashbackType, Map<Commerciant, List<Double>>>
    getCashbackRates(final Account account) {
        return cashbackMap.get(account);
    }

    /**
     * Get the cashback rate.
     * @param account The account.
     * @param type The type of cashback.
     * @return The rate.
     */
    public final Double getCashbackRate(
            final Account account,
            final CashbackType type
    ) {
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

            for (Double rateValue : rateValues) {
                rate += rateValue;
            }
        }

        if (rate > 1.0) {
            return 1.0;
        }

        return rate;
    }

    /**
     * Delete a cashback rate.
     * @param account The account.
     * @param type The type of cashback.
     */
    public final void deleteCashbackRate(
            final Account account,
            final CashbackType type
    ) {
        Map<Commerciant, List<Double>> rates = getCashbackRates(account, type);

        if (rates == null) {
            return;
        }

        for (Commerciant commerciant : rates.keySet()) {
            List<Double> rateValues = rates.get(commerciant);

            if (rateValues == null) {
                continue;
            }

            while (!rateValues.isEmpty()) {
                rateValues.removeFirst();
            }
        }
    }

    /**
     * Check if a cashback was used.
     * @param account The account.
     * @param cashbackType The type of cashback.
     * @return TRUE if the cashback was already used,
     * FALSE OTHERWISE.
     */
    public final boolean wasCashbackUsed(
            final Account account,
            final CashbackType cashbackType
    ) {
        Set<CashbackType> set = usedCashbacksMap.get(account);

        if (set == null) {
            return false;
        }

        return set.contains(cashbackType);
    }

    /**
     * Adds a new cashback for an account given by a commerciant.
     * @param account The account.
     * @param rate The rate.
     * @param type The type of cashback.
     * @param commerciant The commerciant.
     */
    public final void addCashbackRate(
            final Account account,
            final Double rate,
            final CashbackType type,
            final Commerciant commerciant
    ) {
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

        if (!wasCashbackUsed(account, type)) {
            rateValues.add(rate);

            Set<CashbackType> set = usedCashbacksMap.get(account);
            if (set == null) {
                set = new HashSet<>();
                usedCashbacksMap.put(account, set);
            }

            set.add(type);
        }
    }
}
