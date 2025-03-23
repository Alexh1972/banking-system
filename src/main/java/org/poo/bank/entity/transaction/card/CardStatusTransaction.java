package org.poo.bank.entity.transaction.card;

import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;

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
