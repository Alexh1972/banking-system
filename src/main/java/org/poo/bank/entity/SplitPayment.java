package org.poo.bank.entity;

import lombok.Data;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.user.User;

import java.util.*;

@Data
public class SplitPayment {
    private List<String> ibans;
    private Map<String, Double> amounts;
    private List<String> acceptedIbans;
    private Set<String> ibanSet;
    private String currency;

    public SplitPayment(List<String> ibans, List<Double> amounts, String currency) {
        this.ibans = ibans;
        this.amounts = new HashMap<>();
        acceptedIbans = new ArrayList<>();
        ibanSet = new HashSet<>();
        this.currency = currency;

        ibanSet.addAll(ibans);
        for (int i = 0; i < ibans.size(); i++) {
            this.amounts.put(ibans.get(i), amounts.get(i));
        }
    }

    public boolean ibanExists(String iban) {
        return ibanSet.contains(iban);
    }

    public Double getAmount(String iban) {
        return amounts.get(iban);
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
}
