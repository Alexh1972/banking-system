package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.fileio.CommandInput;

public class CardPaymentAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null)
                throw new RuntimeException("User not found");

            Card card = bank.getCard(commandInput.getCardNumber());

            if (card == null)
                throw new RuntimeException("Card not found");

            if (!bank.getUser(card.getOwnerEmail()).equals(bank.getCardUser(card))) {
                throw new RuntimeException("User not found");
            }

            Account account = bank.getAccount(card);

            Double amountSpent = bank.getAmount(commandInput.getAmount(),
                    commandInput.getCurrency(),
                    account.getCurrency());

            if (amountSpent > account.getBalance()) {
                // transaction
            } else {
                account.subtractBalance(amountSpent, card);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
