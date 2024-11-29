package org.poo.bank.entity.transaction;

import org.poo.bank.entity.account.card.CardStatus;

public class CardStatusTransaction extends Transaction {
    public CardStatusTransaction(TransactionMessage description, Integer timestamp) {
        super(description.getValue(), timestamp);
    }

    public CardStatusTransaction(TransactionMessage description, CardStatus cardStatus, Integer timestamp) {
        super(description.getValue().replace("{status}", cardStatus.getValue()), timestamp);
    }
}
