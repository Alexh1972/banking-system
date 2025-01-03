package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.SplitPayment;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.SplitPaymentTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AcceptSplitPaymentAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        User user = bank.getUser(commandInput.getEmail());
        SplitPayment splitPayment = bank.getSplitPayment(user);
        Account account = splitPayment.getAccount(user);

        splitPayment.accept(account);
        if (splitPayment.isComplete()) {
            TransactionNotifier notifier = new TransactionNotifier();
            boolean isPaymentValid = true;
            for (String iban : splitPayment.getIbans()) {
                Account payer = bank.getAccount(iban);
                Double amount = bank.getAmount(
                        splitPayment.getAmount(iban),
                        splitPayment.getCurrency(),
                        payer.getCurrency()
                );

                if (!payer.canPay(amount)) {
                    isPaymentValid = false;
                }

                User notifiedUser = bank.getUser(payer);
                notifier.addListener(notifiedUser, payer);
            }

            if (isPaymentValid) {
                for (String iban : splitPayment.getIbans()) {
                    Account payer = bank.getAccount(iban);
                    Double amount = bank.getAmount(
                            splitPayment.getAmount(iban),
                            splitPayment.getCurrency(),
                            payer.getCurrency()
                    );

                    payer.subtractBalance(amount);
                }

                Transaction transaction =
                        new SplitPaymentTransaction(
                                splitPayment.getAmount(),
                                splitPayment.getCurrency(),
                                splitPayment.getIbans().size(),
                                splitPayment.getIbans(),
                                splitPayment.getTimestamp(),
                                splitPayment.getType(),
                                splitPayment.getAmounts());
                notifier.notifyUsers(transaction);
            }

            bank.removeSplitPayment(splitPayment);
        }
        return null;
    }
}
