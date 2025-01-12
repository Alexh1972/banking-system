package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.SplitPayment;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.SplitPaymentErrorTransaction;
import org.poo.bank.entity.transaction.SplitPaymentTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class AcceptSplitPaymentAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null) {
                throw new RuntimeException("User not found");
            }

            User user = bank.getUser(commandInput.getEmail());

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            SplitPayment splitPayment =
                    bank.getSplitPayment(user, commandInput.getSplitPaymentType());

            if (splitPayment == null) {
                return null;
            }

            Account account = splitPayment.getAccount(user);

            splitPayment.accept(account);
            if (splitPayment.isComplete()) {
                TransactionNotifier notifier = new TransactionNotifier();
                boolean isPaymentValid = true;
                String errorIban = null;
                for (String iban : splitPayment.getIbans()) {
                    Account payer = bank.getAccount(iban);
                    Double amount = bank.getAmount(
                            splitPayment.getAmount(iban),
                            splitPayment.getCurrency(),
                            payer.getCurrency()
                    );

                    if (!payer.canPay(amount)) {
                        isPaymentValid = false;
                        if (errorIban == null) {
                            errorIban = payer.getIban();
                        }
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
                } else {
                    Transaction transaction =
                            new SplitPaymentErrorTransaction(
                                    splitPayment.getAmount(),
                                    splitPayment.getCurrency(),
                                    splitPayment.getIbans().size(),
                                    splitPayment.getIbans(),
                                    errorIban,
                                    splitPayment.getTimestamp(),
                                    splitPayment.getType(),
                                    splitPayment.getAmounts(),
                                    TransactionMessage.TRANSACTION_MESSAGE_SPLIT_PAYMENT_ERROR
                                            .getValue()
                                            .replace("{IBAN}", errorIban));
                    notifier.notifyUsers(transaction);
                }

                bank.removeSplitPayment(splitPayment);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
