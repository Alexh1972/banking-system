package org.poo.bank.entity.account;

import lombok.Getter;
import org.poo.bank.BankSingleton;

@Getter
public enum ServicePlan {
    STANDARD("standard", 0.2 / 100, 0.0),
    STUDENT("student", 0.0, 0.0),
    SILVER("silver", 0.1 / 100, 500.0),
    GOLD("gold", 0.0, 0.0);

    private Double rate;
    private Double threshold;
    private String name;

    ServicePlan(String name, Double rate, Double threshold) {
        this.rate = rate;
        this.threshold = threshold;
        this.name = name;
    }

    public static ServicePlan getServicePlan(String str) {
        for (ServicePlan servicePlan : ServicePlan.values()) {
            if (servicePlan.name.equals(str))
                return servicePlan;
        }

        return STANDARD;
    }

    public static Double getUpgradeFee(ServicePlan oldService, ServicePlan newService) {
        if (oldService.equals(STUDENT) || oldService.equals(STANDARD)) {
            if (newService.equals(SILVER))
                return 100.0;
            if (newService.equals(GOLD))
                return 350.0;
        } else if (oldService.equals(SILVER)) {
            if (newService.equals(GOLD))
                return 250.0;
        }

        return 0.0;
    }

    public static boolean isUpgrade(ServicePlan oldService, ServicePlan newService) {
        if (oldService.equals(STUDENT) || oldService.equals(STANDARD)) {
            return newService.equals(GOLD) || newService.equals(SILVER);
        } else if (oldService.equals(SILVER)) {
            return newService.equals(GOLD);
        }

        return false;
    }

    public static String getFeeCurrency() {
        return "RON";
    }

    public Double getRate(Double amount, String currency) {
        amount = BankSingleton.getInstance().getAmount(amount, currency, getFeeCurrency());
        if (amount >= this.getThreshold()) {
            return this.getRate();
        }

        return 0.0;
    }

    public Double getCommission(Double amount, String currency) {
        return amount * getRate(amount, currency);
    }
}
