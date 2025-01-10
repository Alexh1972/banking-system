package org.poo.bank.strategy.cashback;

import lombok.Getter;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.CommerciantType;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.CardPaymentTransaction;
import org.poo.bank.entity.transaction.CommerciantTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CashbackStrategy {
    private static NumberTransactionsStrategy numberTransactionStrategy = null;
    private static SpendingThresholdStrategy spendingThreasholdStrategy = null;

    public abstract Double getCashbackRate(Account account, Commerciant commerciant, Double amount);
    public abstract void updateCashback(Account account, Commerciant commerciant);
    public Double getCashBackAmount(Account account, Commerciant commerciant, Double amount) {
        return amount * getCashbackRate(account, commerciant, amount);
    }

    public static CashbackStrategy getCashbackStrategy(String str) {
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
        }

        return null;
    }

    protected static List<Transaction> getTransactions(Account user, Commerciant commerciant) {
         return user.getTransactions().stream()
                .filter(t -> t instanceof CommerciantTransaction)
                 .filter(t -> ((CommerciantTransaction)t).isForCommerciant())
                 .filter(t -> ((CommerciantTransaction)t).getCommerciant().equals(commerciant.getName()))
                .toList();
    }

    protected static List<Transaction> getTransactions(Account user, CashbackStrategy type) {
        return user.getTransactions().stream()
                .filter(t -> t instanceof CommerciantTransaction)
                .filter(t -> ((CommerciantTransaction)t).isForCommerciant())
                .filter(t -> BankSingleton.getInstance().getCommerciant(((CommerciantTransaction)t).getCommerciant()).getCashbackStrategy().equals(type))
                .toList();
    }



}
