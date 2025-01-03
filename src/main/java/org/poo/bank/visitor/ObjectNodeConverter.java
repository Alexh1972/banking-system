package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
        objectNode.put("balance", BigDecimal.valueOf(account.getBalance()).setScale(2, RoundingMode.HALF_UP).doubleValue());
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

        objectNode.put("currency", transaction.getCurrency());
        objectNode.put("splitPaymentType", transaction.getType());

        ArrayNode arrayNode = MAPPER.createArrayNode();
        for (String account : transaction.getInvolvedAccounts()) {
            arrayNode.add(account);
        }

        objectNode.put("involvedAccounts", arrayNode);

        ArrayNode amountNode = MAPPER.createArrayNode();
        for (Double account : transaction.getAmounts()) {
            amountNode.add(account);
        }

        objectNode.put("amountForUsers", amountNode);
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

    @Override
    public ObjectNode toObjectNode(WithdrawSavingsTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("classicAccountIBAN", transaction.getClassicAccountIBAN());
        objectNode.put("amount", transaction.getAmount());
        objectNode.put("savingsAccountIBAN", transaction.getSavingsAccountIBAN());
        return objectNode;
    }

    @Override
    public ObjectNode toObjectNode(UpgradePlanTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("newPlanType", transaction.getNewPlanType());
        objectNode.put("accountIBAN", transaction.getAccountIban());
        return objectNode;
    }

    @Override
    public ObjectNode toObjectNode(CashWithdrawalTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("amount", transaction.getAmount());
        return objectNode;
    }

    @Override
    public ObjectNode toObjectNode(AddInterestTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("amount", transaction.getAmount());
        objectNode.put("currency", transaction.getCurrency());
        return objectNode;
    }
}
