package org.poo.bank.entity.transaction;

import org.poo.bank.entity.account.card.CardStatus;

public class CardStatusTransaction extends Transaction {
    public CardStatusTransaction(final TransactionMessage description,
                                 final Integer timestamp) {
        super(description.getValue(), timestamp);
    }

    public CardStatusTransaction(final TransactionMessage description,
                                 final CardStatus cardStatus,
                                 final Integer timestamp) {
        super(description.getValue().replace("{status}", cardStatus.getValue()), timestamp);
    }
}
