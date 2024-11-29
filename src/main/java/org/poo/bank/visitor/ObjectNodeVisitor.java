package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;

import java.util.List;

public interface ObjectNodeVisitor {
    ObjectNode toObjectNode(User user);
    ObjectNode toObjectNode(Transaction transaction);
    ObjectNode toObjectNode(Account account);
    ObjectNode toObjectNode(Card card);
    ObjectNode toObjectNode(CreateAccountTransaction transaction);
    ObjectNode toObjectNode(CreateCardTransaction transaction);
    ObjectNode toObjectNode(DeleteCardTransaction transaction);
    ObjectNode toObjectNode(InsufficientFundsTransaction transaction);
    ObjectNode toObjectNode(SendMoneyTransaction transaction);
    ObjectNode toObjectNode(CardPaymentTransaction transaction);
    ObjectNode toObjectNode(SplitPaymentTransaction transaction);
    ObjectNode toObjectNode(SplitPaymentErrorTransaction transaction);
    ArrayNode toArrayNode(List<Transaction> transactionList);
}
