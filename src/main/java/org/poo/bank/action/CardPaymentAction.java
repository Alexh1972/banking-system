package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.card.Card;
import org.poo.fileio.CommandInput;

public class CardPaymentAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Card card = bank.getCard(commandInput.getCardNumber());

            if (!card.getOwner().equals(bank.getCardUser(card)))
                throw new RuntimeException("Card owner is different");
        } catch (RuntimeException e) {

        }
        return null;
    }
}
