package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.account.card.CardType;
import org.poo.bank.entity.transaction.CreateCardTransaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.notification.TransactionNotifier;
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

            if (user == null) {
                return null;
//                throw new RuntimeException("User not found");
            }

            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                TransactionNotifier.notify(
                        new CreateCardTransaction(
                                TransactionMessage.TRANSACTION_MESSAGE_CARD_DELETED_ERROR_OWNER,
                                commandInput.getTimestamp()),
                        user,
                        null);
                return null;
            }

            Card card = Card.builder()
                    .cardNumber(Utils.generateCardNumber())
                    .status(CardStatus.CARD_STATUS_ACTIVE)
                    .type(cardType)
                    .ownerEmail(user.getEmail())
                    .build();


            bank.addCard(account, card);
            TransactionNotifier.notify(new CreateCardTransaction(
                        account.getIBAN(),
                        card.getCardNumber(),
                        user.getEmail(),
                        commandInput.getTimestamp()),
                    user,
                    account);
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
