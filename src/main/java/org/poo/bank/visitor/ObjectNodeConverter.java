package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;

import java.util.List;

public class ObjectNodeConverter implements ObjectNodeVisitor {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Converts a user to an object node.
     * @param user The user.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final User user) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("firstName", user.getFirstName());
        objectNode.put("lastName", user.getLastName());
        objectNode.put("email", user.getEmail());

        ArrayNode arrayNode = MAPPER.createArrayNode();
        if (user.getAccounts() != null) {
            for (Account account : user.getAccounts()) {
                ObjectNode node = account.accept(this);
                if (node != null) {
                    arrayNode.add(node);
                }
            }
        }

        objectNode.put("accounts", arrayNode);
        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final Transaction transaction) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("timestamp", transaction.getTimestamp());
        objectNode.put("description", transaction.getDescription());

        return objectNode;
    }

    /**
     * Converts an account to an object node.
     * @param account The account.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final Account account) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("IBAN", account.getIban());
        objectNode.put("balance", account.getBalance());
        objectNode.put("currency", account.getCurrency());
        objectNode.put("type", account.getAccountType().getValue());

        ArrayNode arrayNode = MAPPER.createArrayNode();

        if (account.getCards() != null) {
            for (Card card : account.getCards()) {
                ObjectNode node = card.accept(this);
                if (node != null) {
                    arrayNode.add(node);
                }
            }
        }

        objectNode.put("cards", arrayNode);
        return objectNode;
    }

    /**
     * Converts a card to an object node.
     * @param card The card.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final Card card) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("cardNumber", card.getCardNumber());
        objectNode.put("status", card.getStatus().getValue());

        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final CreateAccountTransaction transaction) {
        return toObjectNode((Transaction) transaction);
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final CreateCardTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("account", transaction.getAccount());
        objectNode.put("card", transaction.getCard());
        objectNode.put("cardHolder", transaction.getCardHolder());

        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final InsufficientFundsTransaction transaction) {
        return toObjectNode((Transaction) transaction);
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final SendMoneyTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);
        objectNode.put("amount", transaction.getAmount());
        objectNode.put("receiverIBAN", transaction.getReceiverIBAN());
        objectNode.put("senderIBAN", transaction.getSenderIBAN());
        objectNode.put("transferType", transaction.getTransferType().getValue());
        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final CardPaymentTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("commerciant", transaction.getCommerciant());
        objectNode.put("amount", transaction.getAmount());

        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final DeleteCardTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("account", transaction.getAccount());
        objectNode.put("card", transaction.getCard());
        objectNode.put("cardHolder", transaction.getCardHolder());

        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final SplitPaymentTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("amount", transaction.getAmount());
        objectNode.put("currency", transaction.getCurrency());

        ArrayNode arrayNode = MAPPER.createArrayNode();
        for (String account : transaction.getInvolvedAccounts()) {
            arrayNode.add(account);
        }

        objectNode.put("involvedAccounts", arrayNode);
        return objectNode;
    }

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    @Override
    public final ObjectNode toObjectNode(final SplitPaymentErrorTransaction transaction) {
        ObjectNode objectNode = toObjectNode((SplitPaymentTransaction) transaction);

        objectNode.put("error", transaction.getError());
        return objectNode;
    }

    /**
     * Converts a transaction list to an array node.
     * @param transactionList The transaction list.
     * @return The array node.
     */
    @Override
    public final ArrayNode toArrayNode(final List<Transaction> transactionList) {
        ArrayNode arrayNode = MAPPER.createArrayNode();

        for (Transaction transaction : transactionList) {
            arrayNode.add(transaction.accept(this));
        }

        return arrayNode;
    }
}
