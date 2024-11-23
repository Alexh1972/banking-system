package org.poo.bank.entity.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

@Data
@Builder
public class Card implements ObjectNodeAcceptor {
    private String cardNumber;
    private String status;

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
