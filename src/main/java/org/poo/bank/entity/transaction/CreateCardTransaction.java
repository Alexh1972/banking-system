package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

@Setter
@Getter
public class CreateCardTransaction extends Transaction implements ObjectNodeAcceptor {
    private String account;
    private String card;
    private String cardHolder;
    public CreateCardTransaction(String account, String card, String cardHolder, Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_CREATED.getValue(), timestamp);
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
    }
    public CreateCardTransaction(TransactionMessage message, Integer timestamp) {
        super(message.getValue(), timestamp);
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
