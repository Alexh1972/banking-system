package org.poo.bank.entity.account;

import lombok.Data;
import lombok.Getter;
import org.poo.bank.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class Associate {
    private User user;
    private AssociateType type;
    private List<AssociateInformation> deposited;
    private List<AssociateInformation> spent;

    public Associate(final User user, final String type) {
        this(user, AssociateType.getAssociateType(type));
    }

    public Associate(final User user, final AssociateType type) {
        this.user = user;
        this.type = type;
        this.deposited = new ArrayList<>();
        this.spent = new ArrayList<>();
    }

    /**
     * Get the amount spent by associate between 2 timestamps.
     * @param start Start timestamp.
     * @param finish Finish timestamp.
     * @return The amount spent;
     */
    public final Double getSpent(final Integer start, final Integer finish) {
        Double spentAmount = 0.0;
        for (AssociateInformation pair : getSpent().stream().filter(
                s -> s.getTimestamp() >= start
                        && s.getTimestamp() <= finish
        ).toList()) {
            spentAmount += pair.getValue();
        }

        return spentAmount;
    }

    /**
     * Get the amount deposited by associate between 2 timestamps.
     * @param start Start timestamp.
     * @param finish Finish timestamp.
     * @return The deposited spent;
     */
    public final Double getDeposited(final Integer start, final Integer finish) {
        Double depositedAmount = 0.0;
        for (AssociateInformation pair : getDeposited().stream().filter(
                s -> s.getTimestamp() >= start
                        && s.getTimestamp() <= finish
        ).toList()) {
            depositedAmount += pair.getValue();
        }

        return depositedAmount;
    }

    /**
     * Adds a new payment to a specific timestamp for a user.
     * @param add The payment amount.
     * @param timestamp The timestamp.
     * @return The information about the payment.
     */
    public final AssociateInformation addPayment(
            final Double add,
            final Integer timestamp
    ) {
        AssociateInformation associateInformation =
                new AssociateInformation(add, timestamp, this.user);
        spent.add(associateInformation);
        return associateInformation;
    }

    /**
     * Adds a new deposit to a specific timestamp.
     * @param add The deposit amount.
     * @param timestamp The timestamp.
     */
    public final void addDeposit(final Double add, final Integer timestamp) {
        deposited.add(new AssociateInformation(add, timestamp));
    }

    @Getter
    public static class AssociateInformation {
        private final Double value;
        private final Integer timestamp;
        private final String user;

        public AssociateInformation(final Double value, final Integer timestamp) {
            this(value, timestamp, null);
        }

        public AssociateInformation(final Double value, final Integer timestamp, final User user) {
            this.value = value;
            this.timestamp = timestamp;
            if (user != null) {
                this.user = user.getEmail();
            } else {
                this.user = null;
            }
        }
    }
    @Getter
    public enum AssociateType {
        OWNER("owner",
                false,
                true,
                true,
                true,
                true,
                true),
        MANAGER("manager",
                false,
                true,
                false,
                true,
                false,
                false),
        EMPLOYEE("employee",
                true,
                false,
                false,
                false,
                false,
                false);

        private final String name;
        private final Boolean hasPaymentLimit;
        private final Boolean canDeleteAnyCard;
        private final Boolean canChangeLimits;
        private final Boolean canAddFunds;
        private final Boolean canAddAssociate;
        private final Boolean canDeleteAccount;

        AssociateType(final String name,
                      final Boolean hasPaymentLimit,
                      final Boolean canDeleteAnyCard,
                      final Boolean canChangeLimits,
                      final Boolean canAddFunds,
                      final Boolean canAddAssociate,
                      final Boolean canDeleteAccount) {
            this.name = name;
            this.hasPaymentLimit = hasPaymentLimit;
            this.canDeleteAnyCard = canDeleteAnyCard;
            this.canChangeLimits = canChangeLimits;
            this.canAddFunds = canAddFunds;
            this.canAddAssociate = canAddAssociate;
            this.canDeleteAccount = canDeleteAccount;
        }

        /**
         * Gets the associate type by its name.
         * @param str The name of the associate type.
         * @return The associate type.
         */
        public static AssociateType getAssociateType(final String str) {
            for (AssociateType associate : AssociateType.values()) {
                if (str.equals(associate.name)) {
                    return associate;
                }
            }

            return EMPLOYEE;
        }

        /**
         * Checks if a type is superior to another type.
         * @param type1 The first type.
         * @param type2 The second type.
         * @return TRUE if the first type is superior to
         * the second type, FALSE otherwise.
         */
        public static boolean isUpgrade(final AssociateType type1, final AssociateType type2) {
            if (type1.equals(OWNER)) {
                return type2.equals(MANAGER) || type2.equals(EMPLOYEE);
            } else if (type1.equals(MANAGER)) {
                return type2.equals(EMPLOYEE);
            } else if (type1.equals(EMPLOYEE)) {
                return false;
            }

            return false;
        }
    }
}
