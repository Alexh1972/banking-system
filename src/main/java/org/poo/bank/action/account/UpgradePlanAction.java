package org.poo.bank.action.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.ServicePlan;
import org.poo.bank.entity.transaction.BaseTransaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.entity.transaction.account.UpgradePlanTransaction;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class UpgradePlanAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                throw new RuntimeException("Account not found");
            }

            User user = bank.getUser(account);

            ServicePlan newServicePlan = ServicePlan.getServicePlan(commandInput.getNewPlanType());
            if (user.getServicePlan().equals(newServicePlan)) {
                TransactionNotifier.notify(new BaseTransaction(
                        "The user already has the " + user.getServicePlan().getName() + " plan.",
                                commandInput.getTimestamp()),
                        user,
                        account
                );
                return null;
            }

            if (!ServicePlan.isUpgrade(user.getServicePlan(), newServicePlan)) {
                return null;
            }

            Double amount = bank.getAmount(
                    ServicePlan.getUpgradeFee(
                            user.getServicePlan(),
                            newServicePlan),
                    ServicePlan.getFeeCurrency(),
                    account.getCurrency());

            if (account.subtractBalance(amount)) {
                user.setPlan(newServicePlan);
                TransactionNotifier.notify(new UpgradePlanTransaction(
                                newServicePlan.getName(),
                                account.getIban(),
                                commandInput.getTimestamp()),
                        user,
                        account);
            } else {
                TransactionNotifier.notify(new BaseTransaction(
                        TransactionMessage
                                .TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS
                                .getValue(),
                                commandInput.getTimestamp()),
                        user,
                        account);
            }

            return null;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
