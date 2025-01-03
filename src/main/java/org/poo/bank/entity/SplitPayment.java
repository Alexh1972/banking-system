package org.poo.bank.entity;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.SplitPaymentTransaction;
import org.poo.bank.entity.user.User;

import java.util.*;

@Data
public class SplitPayment {
    private List<String> ibans;
    private Map<String, Double> amounts;
    private List<String> acceptedIbans;
    private Set<String> ibanSet;
    private String currency;
    private String type;
    private int timestamp;

    public SplitPayment(List<String> ibans, List<Double> amounts, String currency, String type, int timestamp) {
        this.ibans = ibans;
        this.amounts = new HashMap<>();
        acceptedIbans = new ArrayList<>();
        ibanSet = new HashSet<>();
        this.currency = currency;
        this.type = type;
        this.timestamp = timestamp;

        ibanSet.addAll(ibans);
        for (int i = 0; i < ibans.size(); i++) {
            this.amounts.put(ibans.get(i), amounts.get(i));
        }
    }

    public List<Double> getAmounts() {
        List<Double> amountList = new ArrayList<>();

        for (String iban : ibans) {
            amountList.add(amounts.get(iban));
        }

        return amountList;
    }

    public boolean ibanExists(String iban) {
        return ibanSet.contains(iban);
    }

    public Double getAmount(String iban) {
        return amounts.get(iban);
    }

    public Double getAmount() {
        Double sum = 0.0;
        for (String iban : ibans) {
            sum += amounts.get(iban);
        }

        return sum;
    }

    public boolean isComplete() {
        return acceptedIbans.size() == ibans.size();
    }

    public boolean ibanExists(User user) {
        for (Account account : user.getAccounts()) {
            if (ibanExists(account.getIban())) {
                return true;
            }
        }

        return false;
    }

    public Account getAccount(User user) {
        Bank bank = BankSingleton.getInstance();
        for (String iban : ibans) {
            if (!acceptedIbans.contains(iban)) {
                if (bank.isUsersAccount(user, iban)) {
                    return bank.getAccount(iban);
                }
            }
        }

        return null;
    }

    public void accept(Account account) {
        if (!acceptedIbans.contains(account.getIban())) {
            acceptedIbans.add(account.getIban());
        }
    }
}
