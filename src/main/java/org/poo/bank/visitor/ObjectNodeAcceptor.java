package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ObjectNodeAcceptor {
    ObjectNode accept(ObjectNodeVisitor objectNodeVisitor);
}
