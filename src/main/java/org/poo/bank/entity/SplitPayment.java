package org.poo.bank.entity;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
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
    private String type;
    private int timestamp;

    public SplitPayment(
            final List<String> ibans,
            final List<Double> amounts,
            final String currency,
            final String type,
            final int timestamp
    ) {
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

    /**
     * Get the amounts paid.
     * @return The amounts.
     */
    public final List<Double> getAmounts() {
        List<Double> amountList = new ArrayList<>();

        for (String iban : ibans) {
            amountList.add(amounts.get(iban));
        }

        return amountList;
    }

    /**
     * Checks if an account is in the payment.
     * @param iban The IBAN.
     * @return TRUE if account with the IBAN is
     * contributing to the payment, FALSE otherwise.
     */
    public final boolean ibanExists(final String iban) {
        return ibanSet.contains(iban);
    }

    /**
     * Gets the amount spent by an account.
     * @param iban The account.
     * @return The amount.
     */
    public final Double getAmount(final String iban) {
        return amounts.get(iban);
    }

    /**
     * Gets the total amount needed to pay.
     * @return The amount.
     */
    public final Double getAmount() {
        Double sum = 0.0;
        for (String iban : ibans) {
            sum += amounts.get(iban);
        }

        return sum;
    }

    /**
     * Checks if the all users accepted the payment.
     * @return TRUE if all users accepted, FALSE otherwise.
     */
    public final boolean isComplete() {
        return acceptedIbans.size() == ibans.size();
    }

    /**
     * Checks if a user is in the payment.
     * @param user The user.
     * @return TRUE if the user is
     * contributing to the payment, FALSE otherwise.
     */
    public final boolean ibanExists(final User user) {
        for (Account account : user.getAccounts()) {
            if (ibanExists(account.getIban())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the account of the user involved in the payment.
     * @param user The user.
     * @return The account involved.
     */
    public final Account getAccount(final User user) {
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

    /**
     * Accepts the split payment for the account.
     * @param account The account.
     */
    public final void accept(final Account account) {
        if (!acceptedIbans.contains(account.getIban())) {
            acceptedIbans.add(account.getIban());
        }
    }
}
