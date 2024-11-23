package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.Transaction;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Card;

public class ObjectNodeConverter implements ObjectNodeVisitor {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public ObjectNode toObjectNode(User user) {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("firstName", user.getFirstName());
        objectNode.put("lastName", user.getLastName());
        objectNode.put("email", user.getEmail());

        ArrayNode arrayNode = mapper.createArrayNode();
        if (user.getAccounts() != null) {
            for (Account account : user.getAccounts()) {
                ObjectNode node = account.accept(this);
                if (node != null)
                    arrayNode.add(node);
            }
        }

        objectNode.put("accounts", arrayNode);
        return objectNode;
    }

    @Override
    public ObjectNode toObjectNode(Transaction transaction) {
        return null;
    }

    @Override
    public ObjectNode toObjectNode(Account account) {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("IBAN", account.getIBAN());
        objectNode.put("balance", account.getBalance());
        objectNode.put("currency", account.getCurrency());
        objectNode.put("type", account.getAccountType().getValue());

        ArrayNode arrayNode = mapper.createArrayNode();

        if (account.getCards() != null) {
            for (Card card : account.getCards()) {
                ObjectNode node = card.accept(this);
                if (node != null)
                    arrayNode.add(node);
            }
        }

        objectNode.put("cards", arrayNode);
        return objectNode;
    }

    @Override
    public ObjectNode toObjectNode(Card card) {
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("cardNumber", card.getCardNumber());
        objectNode.put("status", card.getStatus());

        return objectNode;
    }
}
