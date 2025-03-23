package org.poo.bank.entity.account;

import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.user.User;

import java.util.*;

@Data
public class Associates {
    private static final Double DEFAULT_LIMIT = 500.0;
    private List<Associate> associates;
    private Map<User, Associate> associateMap;
    private Map<Commerciant, List<Associate.AssociateInformation>> informationMap;
    private Double paymentLimit;
    private Double depositLimit;
    private String currency;
    private String iban;

    public Associates(final Account account) {
        Bank bank = BankSingleton.getInstance();
        associates = new ArrayList<>();
        associateMap = new HashMap<>();
        informationMap = new HashMap<>();
        paymentLimit = bank.getAmount(DEFAULT_LIMIT, getDefaultCurrency(), account.getCurrency());
        depositLimit = bank.getAmount(DEFAULT_LIMIT, getDefaultCurrency(), account.getCurrency());
        iban = account.getIban();
        currency = "RON";
    }

    /**
     * Adds a new associate.
     * @param user The user.
     * @param type The association type.
     */
    public final void addAssociate(final User user, final Associate.AssociateType type) {
        Associate associate = new Associate(user, type);

        if (!associateMap.containsKey(user)) {
            associates.add(associate);
            associateMap.put(user, associate);
        }
    }

    /**
     * Adds a new associate.
     * @param user The user.
     * @param type The association type.
     */
    public final void addAssociate(final User user, final String type) {
        addAssociate(user, Associate.AssociateType.getAssociateType(type));
    }

    /**
     * Changes the deposit limit if user has permission.
     * @param user The user.
     * @param amount The new deposit limit.
     * @return TRUE if operation was done successfully,
     * FALSE otherwise.
     */
    public final boolean changeDepositLimit(final User user, final Double amount) {
        if (getAssociate(user).getType().getCanChangeLimits()) {
            depositLimit = amount;
            return true;
        }

        return false;
    }

    /**
     * Changes the payment limit if user has permission.
     * @param user The user.
     * @param amount The new payment limit.
     * @return TRUE if operation was done successfully,
     * FALSE otherwise.
     */
    public final boolean changePaymentLimit(final User user, final Double amount) {
        Associate associate = getAssociate(user);
        if (associate == null) {
            return true;
        }

        if (associate.getType().getCanChangeLimits()) {
            paymentLimit = amount;
            return true;
        }

        return false;
    }

    /**
     * Gets the associate.
     * @param user The user associate.
     * @return The associate.
     */
    public final Associate getAssociate(final User user) {
        return associateMap.get(user);
    }

    /**
     * Checks if user can pay a specific amount.
     * @param user The user.
     * @param amount The amount.
     * @return TRUE if user has permission to pay,
     * FALSE otherwise.
     */
    public final boolean canPay(final User user, final Double amount) {
        Associate associate = getAssociate(user);
        Associate.AssociateType associateType = associate.getType();

        return !associateType.getHasPaymentLimit() ||  amount <= paymentLimit;
    }

    /**
     * Updates associate payments.
     * @param user The user.
     * @param amount The amount paid.
     * @param timestamp The timestamp.
     */
    public final void updateAssociatePayment(
            final User user,
            final Double amount,
            final Integer timestamp
    ) {
        Associate associate = getAssociate(user);
        associate.addPayment(amount, timestamp);
    }

    /**
     * Updates associate payments.
     * @param user The user.
     * @param amount The amount paid.
     * @param commerciant The commerciant paid.
     * @param timestamp The timestamp.
     */
    public final void updateAssociatePayment(
            final User user,
            final Double amount,
            final Commerciant commerciant,
            final Integer timestamp
    ) {
        Associate associate = getAssociate(user);
        Associate.AssociateInformation associateInformation =
                associate.addPayment(amount, timestamp);

        List<Associate.AssociateInformation> list =
                informationMap.computeIfAbsent(commerciant,
                        k -> new ArrayList<>());

        list.add(associateInformation);
    }

    /**
     * Gets the payments for a specific commerciant.
     * @param commerciant The commerciant.
     * @param start The start timestamp.
     * @param end The end timestamp.
     * @return A list of all payments for the commerciant.
     */
    public final List<Associate.AssociateInformation>
    getCommerciantSpending(
            final Commerciant commerciant,
            final Integer start,
            final Integer end
    ) {
        List<Associate.AssociateInformation> list = informationMap.get(commerciant);

        if (list == null) {
            return null;
        }

        return list.stream()
                .filter(a -> a.getTimestamp() >= start && a.getTimestamp() <= end)
                .toList();
    }

    /**
     * Get commerciants paid by the associates.
     * @return The commerciant list.
     */
    public final List<Commerciant> getCommerciantList() {
        return informationMap.keySet().stream()
                .sorted(Comparator.comparing(Commerciant::getName))
                .toList();
    }

    /**
     * Updates the deposit of a user.
     * @param user The user.
     * @param amount The amount deposited.
     * @param timestamp The timestamp.
     */
    public final void updateAssociateDeposit(
            final User user,
            final Double amount,
            final Integer timestamp
    ) {
        Associate associate = getAssociate(user);
        associate.addDeposit(amount, timestamp);
    }

    /**
     * Checks if user has permission to add funds.
     * @param user The user.
     * @param amount The amount.
     * @return TRUE if user can add funds,
     * FALSE otherwise.
     */
    public final boolean canAddFunds(final User user, final Double amount) {
        Associate.AssociateType associateType = getAssociate(user).getType();
        return associateType.getCanAddFunds() || amount <= depositLimit;
    }

    /**
     * Get the default currency for associates.
     * @return The currency.
     */
    public final String getDefaultCurrency() {
        return "RON";
    }
}
