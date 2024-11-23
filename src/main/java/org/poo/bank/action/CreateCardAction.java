package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateCardAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        Account account = bank.getAccountIBANMap().get(commandInput.getAccount());

        if (account == null)
            throw new RuntimeException("Account with IBAN '" + commandInput.getEmail() + "' not found");

        Card card = Card.builder()
                .cardNumber(Utils.generateCardNumber())
                .status("active")
                .build();

        if (account.getCards() != null)
            account.getCards().add(card);

        return null;
    }
}
