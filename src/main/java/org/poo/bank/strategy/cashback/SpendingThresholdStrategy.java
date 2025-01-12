package org.poo.bank.strategy.cashback;

import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.CashbackType;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.ServicePlan;
import org.poo.bank.entity.transaction.CommerciantTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.user.User;

import java.util.List;

public class SpendingThresholdStrategy extends CashbackStrategy {
    private static final Double POINT_TWENTY_FIVE_PERCENT = 0.25 / 100;
    private static final Double POINT_TWENTY_PERCENT = 0.2 / 100;
    private static final Double POINT_FORTY_PERCENT = 0.4 / 100;
    private static final Double POINT_TEN_PERCENT = 0.1 / 100;
    private static final Double POINT_FIFTY_PERCENT = 0.5 / 100;
    private static final Double POINT_THIRTY_PERCENT = 0.3 / 100;
    private static final Double POINT_SEVENTY_PERCENT = 0.7 / 100;
    private static final Double POINT_FIFTY_FIVE_PERCENT = 0.55 / 100;
    private static final Integer SMALL_THRESHOLD = 100;
    private static final Integer MEDIUM_THRESHOLD = 300;
    private static final Integer BIG_THRESHOLD = 500;
    /**
     * {@inheritDoc}
     */
    @Override
    public final Double getCashbackRate(
            final Account account,
            final Commerciant commerciant,
            final Double amount) {
        Bank bank = BankSingleton.getInstance();
        User user = bank.getUser(account);
        List<Transaction> transactions =
                getTransactions(
                        account,
                        CashbackStrategy.getCashbackStrategy("spendingThreshold")
                );

        Double sum = bank.getAmount(
                amount,
                account.getCurrency(),
                "RON"
        );
        for (Transaction transaction : transactions) {
            CommerciantTransaction paymentTransaction = (CommerciantTransaction) transaction;
            sum += bank.getAmount(
                    paymentTransaction.getAmountSpent(),
                    paymentTransaction.getCurrencyUsed(),
                    "RON"
            );
        }

        double rate = 0.0;
        if (user.getServicePlan()
                .equals(ServicePlan.STANDARD)
                || user.getServicePlan()
                .equals(ServicePlan.STUDENT)
        ) {
            if (sum >= BIG_THRESHOLD) {
                rate = POINT_TWENTY_FIVE_PERCENT;
            } else if (sum >= MEDIUM_THRESHOLD) {
                rate = POINT_TWENTY_PERCENT;
            } else if (sum >= SMALL_THRESHOLD) {
                rate = POINT_TEN_PERCENT;
            }
        } else if (user.getServicePlan().equals(ServicePlan.SILVER)) {
            if (sum >= BIG_THRESHOLD) {
                rate = POINT_FIFTY_PERCENT;
            } else if (sum >= MEDIUM_THRESHOLD) {
                rate = POINT_FORTY_PERCENT;
            } else if (sum >= SMALL_THRESHOLD) {
                rate = POINT_THIRTY_PERCENT;
            }
        } else if (user.getServicePlan().equals(ServicePlan.GOLD)) {
            if (sum >= BIG_THRESHOLD) {
                rate = POINT_SEVENTY_PERCENT;
            } else if (sum >= MEDIUM_THRESHOLD) {
                rate = POINT_FIFTY_FIVE_PERCENT;
            } else if (sum >= SMALL_THRESHOLD) {
                rate = POINT_FIFTY_PERCENT;
            }
        }

        Double typeRate = bank.getCashbackRates().getCashbackRate(account,
                CashbackType.getCashbackType(commerciant.getType().getValue()));

        return rate + typeRate;
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
                                commerciant.getType().getValue()
                        )
                );
    }
}
