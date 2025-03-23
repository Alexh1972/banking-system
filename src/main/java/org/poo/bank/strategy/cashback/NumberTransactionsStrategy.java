package org.poo.bank.strategy.cashback;

import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.CashbackType;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.Transaction;

import java.util.List;

public class NumberTransactionsStrategy extends CashbackStrategy {
    private static final Double FIVE_PERCENT = 5.0 / 100;
    private static final Double TEN_PERCENT = 10.0 / 100;
    private static final Double TWO_PERCENT = 2.0 / 100;

    private static final Integer FIVE_PERCENT_THRESHOLD = 5;
    private static final Integer TEN_PERCENT_THRESHOLD = 10;
    private static final Integer TWO_PERCENT_THRESHOLD = 2;

    /**
     * {@inheritDoc}
     */
    @Override
    public final Double getCashbackRate(
            final Account account,
            final Commerciant commerciant,
            final Double amount
    ) {
        Bank bank = BankSingleton.getInstance();
        return bank.getCashbackRates().getCashbackRate(account,
                CashbackType.getCashbackType(commerciant.getType().getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void updateCashback(final Account account, final Commerciant commerciant) {
        Bank bank = BankSingleton.getInstance();
        bank.getCashbackRates()
                .deleteCashbackRate(
                        account,
                        CashbackType.getCashbackType(
                                        commerciant
                                                .getType()
                                                .getValue()
                                )
                );
        List<Transaction> transactions = getTransactions(account, commerciant);
        CashbackType cashbackType = null;

        int size = transactions.size() + 1;
        double rate = 0.0;
        if (size >= TEN_PERCENT_THRESHOLD) {
            rate = TEN_PERCENT;
            cashbackType = CashbackType.FOR_TECH;
        } else if (size >= FIVE_PERCENT_THRESHOLD) {
            rate = FIVE_PERCENT;
            cashbackType = CashbackType.FOR_CLOTHES;
        } else if (size >= TWO_PERCENT_THRESHOLD) {
            rate = TWO_PERCENT;
            cashbackType = CashbackType.FOR_FOOD;
        }

        List<Double> rates = bank.getCashbackRates()
                .getCashbackRates(
                        account,
                        cashbackType,
                        commerciant
                );
        if (rate != 0.0) {
            if (rates == null
                    || !rates.contains(rate)) {
                bank.getCashbackRates()
                        .addCashbackRate(
                                account,
                                rate,
                                cashbackType,
                                commerciant
                        );
            }
        }
    }
}
