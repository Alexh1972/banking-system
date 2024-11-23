package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.Transaction;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Card;

public interface ObjectNodeVisitor {
    ObjectNode toObjectNode(User user);
    ObjectNode toObjectNode(Transaction transaction);
    ObjectNode toObjectNode(Account account);
    ObjectNode toObjectNode(Card card);
}
