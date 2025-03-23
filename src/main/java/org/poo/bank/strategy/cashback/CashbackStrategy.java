package org.poo.bank.strategy.cashback;

import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.CommerciantTransaction;
import org.poo.bank.entity.transaction.Transaction;

import java.util.List;

public abstract class CashbackStrategy {
    private static NumberTransactionsStrategy numberTransactionStrategy = null;
    private static SpendingThresholdStrategy spendingThreasholdStrategy = null;

    /**
     * Gets the cashback rate for an account.
     * @param account The account.
     * @param commerciant The commerciant.
     * @param amount The amount.
     * @return The rate.
     */
    public abstract Double getCashbackRate(Account account, Commerciant commerciant, Double amount);

    /**
     * Updates the cashback rates.
     * @param account The account.
     * @param commerciant The commerciant.
     */
    public abstract void updateCashback(Account account, Commerciant commerciant);

    /**
     * Gets the cashback amount.
     * @param account The account.
     * @param commerciant The commerciant.
     * @param amount The amount spent.
     * @return The cashback amount.
     */
    public final Double getCashBackAmount(
            final Account account,
            final Commerciant commerciant,
            final Double amount
    ) {
        return amount * getCashbackRate(account, commerciant, amount);
    }

    /**
     * Gets the cashback strategy by its name.
     * @param str The name.
     * @return The cashback strategy.
     */
    public static CashbackStrategy getCashbackStrategy(final String str) {
        switch (str) {
            case "nrOfTransactions" -> {
                if (numberTransactionStrategy == null) {
                    numberTransactionStrategy = new NumberTransactionsStrategy();
                }
                return numberTransactionStrategy;
            }
            case "spendingThreshold" -> {
                if (spendingThreasholdStrategy == null) {
                    spendingThreasholdStrategy = new SpendingThresholdStrategy();
                }
                return spendingThreasholdStrategy;
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Gets the transactions of an account.
     * @param user The account.
     * @param commerciant The commerciant.
     * @return The transactions.
     */
    protected static List<Transaction> getTransactions(
            final Account user,
            final Commerciant commerciant
    ) {
         return user.getTransactions().stream()
                 .filter(t -> t instanceof CommerciantTransaction)
                 .filter(t -> ((CommerciantTransaction) t).isForCommerciant())
                 .filter(t -> ((CommerciantTransaction) t)
                         .getCommerciant()
                         .equals(commerciant.getName())
                 )
                 .toList();
    }

    /**
     * Gets the transactions of an account.
     * @param user The account.
     * @param type The type of the commerciants.
     * @return The transactions.
     */
    protected static List<Transaction> getTransactions(
            final Account user,
            final CashbackStrategy type
    ) {
        return user.getTransactions().stream()
                .filter(t -> t instanceof CommerciantTransaction)
                .filter(t -> ((CommerciantTransaction) t).isForCommerciant())
                .filter(t -> BankSingleton
                        .getInstance()
                        .getCommerciant(
                                ((CommerciantTransaction) t)
                                        .getCommerciant()
                        )
                        .getCashbackStrategy()
                        .equals(type)
                )
                .toList();
    }



}
