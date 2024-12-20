package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.transaction.DeleteCardTransaction;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

public class DeleteCardAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null) {
                throw new RuntimeException("User not found");
            }

            Card card = bank.getCard(commandInput.getCardNumber());

            if (card == null) {
                return null;
            }

            Account account = bank.getAccount(card);

            if (account == null) {
                throw new RuntimeException("User not found");
            }

            User user = bank.getUser(account);

            if (user == null || !user.equals(bank.getUser(commandInput.getEmail()))) {
                throw new RuntimeException("User not found");
            }

            bank.deleteCard(card, account);
            TransactionNotifier.notify(
                    new DeleteCardTransaction(
                            account.getIban(),
                            card.getCardNumber(),
                            card.getOwnerEmail(),
                            commandInput.getTimestamp()),
                    user,
                    account);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
