package org.poo.bank.entity.account.card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import org.poo.bank.entity.User;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

@Data
@Builder
public class Card implements ObjectNodeAcceptor {
    private String cardNumber;
    private CardStatus status;
    private CardType type;
    private User owner;

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
