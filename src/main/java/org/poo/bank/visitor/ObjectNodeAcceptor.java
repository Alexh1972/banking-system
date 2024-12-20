package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ObjectNodeAcceptor {
    /**
     * Double dispatch for converting an object to
     * an object node.
     * @param objectNodeVisitor The object node
     *                          converter visitor.
     * @return The object node.
     */
    ObjectNode accept(ObjectNodeVisitor objectNodeVisitor);
}
