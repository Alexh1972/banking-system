package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.account.card.CardType;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateCardAction extends Action {
    private CardType cardType;
    public CreateCardAction(CardType cardType) {
        this.cardType = cardType;
    }
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            User user = bank.getUser(commandInput.getEmail());

            if (user == null)
                throw new RuntimeException("User not found");

            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null)
                throw new RuntimeException("User not found");

            Card card = Card.builder()
                    .cardNumber(Utils.generateCardNumber())
                    .status(CardStatus.getCardStatus("active"))
                    .type(cardType)
                    .owner(user)
                    .build();

            bank.addCard(account, card);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
