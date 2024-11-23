package org.poo.bank.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

public class Transaction implements ObjectNodeAcceptor {
    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
