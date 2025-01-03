package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.account.Associates;

import java.util.List;

public interface AssociateReportConverter {
    ObjectNode toObjectNode(Associates associates, Integer start, Integer finish);
}
