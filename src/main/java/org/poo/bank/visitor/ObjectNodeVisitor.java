package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;

import java.util.List;

public interface ObjectNodeVisitor {
    /**
     * Converts a user to an object node.
     * @param user The user.
     * @return The object node.
     */
    ObjectNode toObjectNode(User user);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(Transaction transaction);

    /**
     * Converts an account to an object node.
     * @param account The account.
     * @return The object node.
     */
    ObjectNode toObjectNode(Account account);

    /**
     * Converts a card to an object node.
     * @param card The card.
     * @return The object node.
     */
    ObjectNode toObjectNode(Card card);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(CreateAccountTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(CreateCardTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(DeleteCardTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(InsufficientFundsTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(SendMoneyTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(CardPaymentTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(SplitPaymentTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(SplitPaymentErrorTransaction transaction);

    /**
     * Converts a transaction list to an array node.
     * @param transactionList The transaction list.
     * @return The array node.
     */
    ArrayNode toArrayNode(List<Transaction> transactionList);

    ObjectNode toObjectNode(WithdrawSavingsTransaction transaction);

    ObjectNode toObjectNode(UpgradePlanTransaction transaction);

    ObjectNode toObjectNode(CashWithdrawalTransaction transaction);

    ObjectNode toObjectNode(AddInterestTransaction transaction);
}
