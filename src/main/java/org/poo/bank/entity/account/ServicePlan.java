package org.poo.bank.entity.account;

import lombok.Getter;
import org.poo.bank.BankSingleton;

@Getter
public enum ServicePlan {
    STANDARD("standard", 0.2 / 100, 0.0),
    STUDENT("student", 0.0, 0.0),
    SILVER("silver", 0.1 / 100, 500.0),
    GOLD("gold", 0.0, 0.0);

    private static final Double STANDARD_SILVER_FEE = 100.0;
    private static final Double STANDARD_GOLD_FEE = 350.0;
    private static final Double SILVER_GOLD_FEE = 250.0;
    private final Double rate;
    private final Double threshold;
    private final String name;

    ServicePlan(final String name, final Double rate, final Double threshold) {
        this.rate = rate;
        this.threshold = threshold;
        this.name = name;
    }

    /**
     * Gets the service plan by iys name.
     * @param str The name.
     * @return The service plan.
     */
    public static ServicePlan getServicePlan(final String str) {
        for (ServicePlan servicePlan : ServicePlan.values()) {
            if (servicePlan.name.equals(str)) {
                return servicePlan;
            }
        }

        return STANDARD;
    }

    /**
     * Gets the upgrade fee.
     * @param oldService The old service owned.
     * @param newService The new service.
     * @return The fee to upgrade.
     */
    public static Double getUpgradeFee(final ServicePlan oldService, final ServicePlan newService) {
        if (oldService.equals(STUDENT) || oldService.equals(STANDARD)) {
            if (newService.equals(SILVER)) {
                return STANDARD_SILVER_FEE;
            }
            if (newService.equals(GOLD)) {
                return STANDARD_GOLD_FEE;
            }
        } else if (oldService.equals(SILVER)) {
            if (newService.equals(GOLD)) {
                return SILVER_GOLD_FEE;
            }
        }

        return 0.0;
    }

    /**
     * Checks if the second service is superior to the first.
     * @param oldService The first service.
     * @param newService The new service.
     * @return TRUE if the second service is superior to the first,
     * FALSE OTHERWISE.
     */
    public static boolean isUpgrade(final ServicePlan oldService, final ServicePlan newService) {
        if (oldService.equals(STUDENT) || oldService.equals(STANDARD)) {
            return newService.equals(GOLD) || newService.equals(SILVER);
        } else if (oldService.equals(SILVER)) {
            return newService.equals(GOLD);
        }

        return false;
    }

    /**
     * Gets the upgrade fee currency.
     * @return The fee.
     */
    public static String getFeeCurrency() {
        return "RON";
    }

    /**
     * Gets the commission rate.
     * @param amount The amount on which commission is calculated.
     * @param currency The currency of the amount.
     * @return The commission rate.
     */
    public final Double getRate(final Double amount, final String currency) {
        Double amountConverted =
                BankSingleton.getInstance()
                        .getAmount(amount,
                                currency,
                                getFeeCurrency()
                        );
        if (amountConverted >= this.getThreshold()) {
            return this.getRate();
        }

        return 0.0;
    }

    /**
     * Gets the commission amount.
     * @param amount The amount on which commission is calculated.
     * @param currency The currency of the amount.
     * @return The commission amount.
     */
    public final Double getCommission(final Double amount, final String currency) {
        return amount * getRate(amount, currency);
    }
}
