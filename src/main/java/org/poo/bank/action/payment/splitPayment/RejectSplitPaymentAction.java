package org.poo.bank.action.payment.splitPayment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.SplitPayment;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.payment.splitPayment.SplitPaymentErrorTransaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class RejectSplitPaymentAction extends Action {
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

            SplitPayment splitPayment = bank.getSplitPayment(user,
                    commandInput.getSplitPaymentType());
            if (splitPayment == null) {
                return null;
            }
            bank.removeSplitPayment(splitPayment);

            TransactionNotifier notifier = new TransactionNotifier();
            for (String iban : splitPayment.getIbans()) {
                Account account = bank.getAccount(iban);
                User userOfAccount = bank.getUser(account);

                notifier.addListener(userOfAccount, account);
            }

            notifier.notifyUsers(new SplitPaymentErrorTransaction(
                   splitPayment.getAmount(),
                    splitPayment.getCurrency(),
                    splitPayment.getIbans().size(),
                    splitPayment.getIbans(),
                    null,
                    splitPayment.getTimestamp(),
                    splitPayment.getType(),
                    splitPayment.getAmounts(),
                    TransactionMessage.TRANSACTION_MESSAGE_REJECTED_SPLIT_PAYMENT.getValue()
            ));
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
