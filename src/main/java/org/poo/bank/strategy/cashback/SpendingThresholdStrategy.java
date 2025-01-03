package org.poo.bank.strategy.cashback;

import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.CashbackType;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.ServicePlan;
import org.poo.bank.entity.transaction.CardPaymentTransaction;
import org.poo.bank.entity.transaction.Transaction;

import java.util.List;

public class SpendingThresholdStrategy extends CashbackStrategy {
    @Override
    public Double getCashbackRate(Account account, Commerciant commerciant, Double amount) {
//        Bank bank = BankSingleton.getInstance();
//
//        Double typeRate = bank.getCashbackRates().getCashbackRate(account,
//                CashbackType.getCashbackType(commerciant.getType().getValue()));
//
//        Double spendingThresholdRate = bank.getCashbackRates().getCashbackRate(account,
//                CashbackType.FOR_SPENDING_THRESHOLD_COMMERCIANT);
//        return typeRate + spendingThresholdRate;
        Bank bank = BankSingleton.getInstance();
        List<Transaction> transactions = getTransactions(account, commerciant);

        Double sum = bank.getAmount(amount, account.getCurrency(), "RON");;
        for (Transaction transaction : transactions) {
            CardPaymentTransaction paymentTransaction = (CardPaymentTransaction) transaction;
            sum += bank.getAmount(paymentTransaction.getAmount(), paymentTransaction.getCurrency(), "RON");
        }

        double rate = 0.0;
        if (account.getServicePlan().equals(ServicePlan.STANDARD) || account.getServicePlan().equals(ServicePlan.STUDENT)) {
            if (sum >= 500) {
                rate = 0.25 / 100;
            } else if (sum >= 300) {
                rate = 0.2 / 100;
            } else if (sum >= 100) {
                rate = 0.1 / 100;
            }
        } else if (account.getServicePlan().equals(ServicePlan.SILVER)) {
            if (sum >= 500) {
                rate = 0.5 / 100;
            } else if (sum >= 300) {
                rate = 0.4 / 100;
            } else if (sum >= 100) {
                rate = 0.3 / 100;
            }
        } else if (account.getServicePlan().equals(ServicePlan.GOLD)) {
            if (sum >= 500) {
                rate = 0.7 / 100;
            } else if (sum >= 300) {
                rate = 0.55 / 100;
            } else if (sum >= 100) {
                rate = 0.5 / 100;
            }
        }

        return rate;
    }

    @Override
    public void updateCashback(Account account, Commerciant commerciant) {
//        Bank bank = BankSingleton.getInstance();
//        List<Transaction> transactions = getTransactions(account, commerciant);
//
//        Double sum = 0.0;
//        for (Transaction transaction : transactions) {
//            CardPaymentTransaction paymentTransaction = (CardPaymentTransaction) transaction;
//            sum += bank.getAmount(paymentTransaction.getAmount(), paymentTransaction.getCurrency(), "RON");
//        }
//
//        double rate = 0.0;
//        if (account.getServicePlan().equals(ServicePlan.STANDARD) || account.getServicePlan().equals(ServicePlan.STUDENT)) {
//            if (sum >= 500) {
//                rate = 0.25 / 100;
//            } else if (sum >= 300) {
//                rate = 0.2 / 100;
//            } else if (sum >= 100) {
//                rate = 0.1 / 100;
//            }
//        } else if (account.getServicePlan().equals(ServicePlan.SILVER)) {
//            if (sum >= 500) {
//                rate = 0.5 / 100;
//            } else if (sum >= 300) {
//                rate = 0.4 / 100;
//            } else if (sum >= 100) {
//                rate = 0.3 / 100;
//            }
//        } else if (account.getServicePlan().equals(ServicePlan.GOLD)) {
//            if (sum >= 500) {
//                rate = 0.7 / 100;
//            } else if (sum >= 300) {
//                rate = 0.55 / 100;
//            } else if (sum >= 100) {
//                rate = 0.5 / 100;
//            }
//        }
//
//        if (rate != 0.0) {
//            List<Double> rates = bank.getCashbackRates().getCashbackRates(account, CashbackType.FOR_SPENDING_THRESHOLD_COMMERCIANT, commerciant);
//
//            if (rates != null) {
//                while (!rates.isEmpty()) {
//                    rates.removeFirst();
//                }
//            }
//
//            bank.getCashbackRates().addCashbackRate(account, rate, CashbackType.FOR_SPENDING_THRESHOLD_COMMERCIANT, commerciant);
//        }
    }
}
