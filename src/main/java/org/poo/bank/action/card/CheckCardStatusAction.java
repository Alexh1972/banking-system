package org.poo.bank.action.card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.transaction.card.CardStatusTransaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class CheckCardStatusAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Card card = bank.getCard(commandInput.getCardNumber());

            if (card == null) {
                throw new RuntimeException("Card not found");
            }

            Account account = bank.getAccount(card);
            User user = bank.getUser(account);
            if (account.isMinimumBalanceReached()) {
                card.setStatus(CardStatus.CARD_STATUS_FROZEN);
                TransactionNotifier.notify(
                        new CardStatusTransaction(
                                TransactionMessage.TRANSACTION_MESSAGE_SET_CARD_TO_FROZEN,
                                commandInput.getTimestamp()),
                        user,
                        account);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
