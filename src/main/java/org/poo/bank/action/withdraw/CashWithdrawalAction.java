package org.poo.bank.action.withdraw;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.transaction.BaseTransaction;
import org.poo.bank.entity.transaction.withdraw.CashWithdrawalTransaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.entity.transaction.TransferType;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class CashWithdrawalAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null || commandInput.getEmail().isEmpty()) {
                throw new RuntimeException("User not found");
            }

            Card card = bank.getCard(commandInput.getCardNumber());

            if (card == null) {
                throw new RuntimeException("Card not found");
            }

            Account account = bank.getAccount(card);
            User user = bank.getUser(account);
            Associates associates = bank.getAssociates(account);
            if (associates == null) {
                if (!bank.getUser(card).getEmail().equals(commandInput.getEmail())) {
                    throw new RuntimeException("Card not found");
                }
            }

            Double amount = bank.getAmount(commandInput.getAmount(), "RON", account.getCurrency());
            Double commissionAmount =
                    amount + user.getServicePlan().getCommission(amount, account.getCurrency());

            TransferType transferType = account.subtractBalance(commissionAmount, card);
            switch (transferType) {
                case TRANSFER_TYPE_SUCCESSFUL -> TransactionNotifier.notify(
                        new CashWithdrawalTransaction(commandInput.getAmount(),
                                commandInput.getTimestamp()),
                        user,
                        account);
                case TRANSFER_TYPE_INSUFFICIENT_FUNDS -> TransactionNotifier.notify(
                        new BaseTransaction(TransactionMessage
                                .TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS
                                .getValue(),
                                commandInput.getTimestamp()),
                        user,
                        account);
                default -> {
                    return null;
                }
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
