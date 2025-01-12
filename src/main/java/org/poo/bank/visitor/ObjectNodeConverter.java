package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.entity.user.User;

import java.util.List;

public class ObjectNodeConverter implements ObjectNodeVisitor {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(final Transaction transaction) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("timestamp", transaction.getTimestamp());
        objectNode.put("description", transaction.getDescription());

        return objectNode;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(final Card card) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("cardNumber", card.getCardNumber());
        objectNode.put("status", card.getStatus().getValue());

        return objectNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(final CreateAccountTransaction transaction) {
        return toObjectNode((Transaction) transaction);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(final InsufficientFundsTransaction transaction) {
        return toObjectNode((Transaction) transaction);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(final CardPaymentTransaction transaction) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("commerciant", transaction.getCommerciant());
        objectNode.put("amount", transaction.getAmount());

        return objectNode;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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

        if (transaction.getType().equals("custom")) {
            ArrayNode amountNode = MAPPER.createArrayNode();
            for (Double account : transaction.getAmounts()) {
                amountNode.add(account);
            }

            objectNode.put("amountForUsers", amountNode);
        } else {
            objectNode.put("amount",  transaction.getAmount() / transaction.getNumberPayers());
        }
        return objectNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(final SplitPaymentErrorTransaction transaction) {
        ObjectNode objectNode = toObjectNode((SplitPaymentTransaction) transaction);

        objectNode.put("error", transaction.getError());
        return objectNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ArrayNode toArrayNode(final List<Transaction> transactionList) {
        ArrayNode arrayNode = MAPPER.createArrayNode();

        for (Transaction transaction : transactionList) {
            arrayNode.add(transaction.accept(this));
        }

        return arrayNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(
            final WithdrawSavingsTransaction transaction
    ) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("classicAccountIBAN", transaction.getClassicAccountIBAN());
        objectNode.put("amount", transaction.getAmount());
        objectNode.put("savingsAccountIBAN", transaction.getSavingsAccountIBAN());
        return objectNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(
            final UpgradePlanTransaction transaction
    ) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("newPlanType", transaction.getNewPlanType());
        objectNode.put("accountIBAN", transaction.getAccountIban());
        return objectNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(
            final CashWithdrawalTransaction transaction
    ) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("amount", transaction.getAmount());
        return objectNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(
            final AddInterestTransaction transaction
    ) {
        ObjectNode objectNode = toObjectNode((Transaction) transaction);

        objectNode.put("amount", transaction.getAmount());
        objectNode.put("currency", transaction.getCurrency());
        return objectNode;
    }
}
