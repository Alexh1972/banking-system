package org.poo.bank.entity.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.poo.bank.entity.user.User;

@Data
public class Associate {
    private User user;
    private AssociateType type;

    public Associate(User user, String type) {
        this.user = user;
        this.type = AssociateType.getAssociateType(type);
    }

    @Getter
    public enum AssociateType {
        OWNER("owner", false, true, true),
        MANAGER("manager", false, true, false),
        EMPLOYEE("employee", false, false, false);

        private final String name;
        private final Boolean hasPaymentLimit;
        private final Boolean canDeleteAnyCard;
        private final Boolean canChangeLimits;

        AssociateType(String name, Boolean hasPaymentLimit, Boolean canDeleteAnyCard, Boolean canChangeLimits) {
            this.name = name;
            this.hasPaymentLimit = hasPaymentLimit;
            this.canDeleteAnyCard = canDeleteAnyCard;
            this.canChangeLimits = canChangeLimits;
        }

        public static AssociateType getAssociateType(String str) {
            for (AssociateType associate : AssociateType.values()) {
                if (str.equals(associate.name))
                    return associate;
            }

            return EMPLOYEE;
        }
    }
}
