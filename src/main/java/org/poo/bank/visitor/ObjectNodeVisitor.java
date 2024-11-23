package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.transaction.CreateAccountTransaction;
import org.poo.bank.entity.transaction.CreateCardTransaction;
import org.poo.bank.entity.transaction.DeleteCardTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;

public interface ObjectNodeVisitor {
    ObjectNode toObjectNode(User user);
    ObjectNode toObjectNode(Transaction transaction);
    ObjectNode toObjectNode(Account account);
    ObjectNode toObjectNode(Card card);
    ObjectNode toObjectNode(CreateAccountTransaction transaction);
    ObjectNode toObjectNode(CreateCardTransaction transaction);
    ObjectNode toObjectNode(DeleteCardTransaction transaction);
}
