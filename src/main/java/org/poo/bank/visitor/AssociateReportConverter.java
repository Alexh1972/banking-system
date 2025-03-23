package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.account.Associates;

public interface AssociateReportConverter {
    /**
     * Converts associates to object node.
     * @param associates The associates.
     * @param start The start timestamp.
     * @param finish The finish timestamp.
     * @return The object node.
     */
    ObjectNode toObjectNode(Associates associates, Integer start, Integer finish);
}
