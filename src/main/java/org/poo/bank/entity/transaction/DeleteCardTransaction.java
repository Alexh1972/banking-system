package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeVisitor;

@Setter
@Getter
public class DeleteCardTransaction extends Transaction {
    private String account;
    private String card;
    private String cardHolder;
    public DeleteCardTransaction(String account, String card, String cardHolder, Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_DELETED.getValue(), timestamp);
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
