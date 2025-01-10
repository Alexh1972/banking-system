package org.poo.bank.entity.account;

import lombok.Data;
import lombok.Getter;
import org.poo.bank.entity.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Associate {
    private User user;
    private AssociateType type;
    private List<AssociateInformation> deposited;
    private List<AssociateInformation> spent;

    public Associate(User user, String type) {
        this(user, AssociateType.getAssociateType(type));
    }

    public Associate(User user, AssociateType type) {
        this.user = user;
        this.type = type;
        this.deposited = new ArrayList<>();
        this.spent = new ArrayList<>();
    }

    public Double getSpent(Integer start, Integer finish) {
        Double spent = 0.0;
        for (AssociateInformation pair : getSpent().stream().filter(
                s -> s.getTimestamp() >= start &&
                        s.getTimestamp() <= finish
        ).toList()) {
            spent += pair.getValue();
        }

        return spent;
    }

    public Double getDeposited(Integer start, Integer finish) {
        Double deposited = 0.0;
        for (AssociateInformation pair : getDeposited().stream().filter(
                s -> s.getTimestamp() >= start &&
                        s.getTimestamp() <= finish
        ).toList()) {
            deposited += pair.getValue();
        }

        return deposited;
    }

    public AssociateInformation addPayment(Double add, Integer timestamp) {
        AssociateInformation associateInformation = new AssociateInformation(add, timestamp);
        spent.add(associateInformation);
        return associateInformation;
    }

    public AssociateInformation addPayment(User user, Double add, Integer timestamp) {
        AssociateInformation associateInformation = new AssociateInformation(add, timestamp, user);
        spent.add(associateInformation);
        return associateInformation;
    }

    public void addDeposit(Double add, Integer timestamp) {
        deposited.add(new AssociateInformation(add, timestamp));
    }

    @Getter
    public static class AssociateInformation {
        private final Double value;
        private final Integer timestamp;
        private final String user;

        public AssociateInformation(Double value, Integer timestamp) {
            this(value, timestamp, null);
        }

        public AssociateInformation(Double value, Integer timestamp, User user) {
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

        AssociateType(String name,
                      Boolean hasPaymentLimit,
                      Boolean canDeleteAnyCard,
                      Boolean canChangeLimits,
                      Boolean canAddFunds,
                      Boolean canAddAssociate,
                      Boolean canDeleteAccount) {
            this.name = name;
            this.hasPaymentLimit = hasPaymentLimit;
            this.canDeleteAnyCard = canDeleteAnyCard;
            this.canChangeLimits = canChangeLimits;
            this.canAddFunds = canAddFunds;
            this.canAddAssociate = canAddAssociate;
            this.canDeleteAccount = canDeleteAccount;
        }

        public static AssociateType getAssociateType(String str) {
            for (AssociateType associate : AssociateType.values()) {
                if (str.equals(associate.name))
                    return associate;
            }

            return EMPLOYEE;
        }

        public static boolean isUpgrade(AssociateType type1, AssociateType type2) {
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
