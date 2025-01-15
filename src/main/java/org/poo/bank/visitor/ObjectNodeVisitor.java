package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.entity.transaction.account.AddInterestTransaction;
import org.poo.bank.entity.transaction.account.CreateAccountTransaction;
import org.poo.bank.entity.transaction.account.UpgradePlanTransaction;
import org.poo.bank.entity.transaction.card.CardPaymentTransaction;
import org.poo.bank.entity.transaction.card.CreateCardTransaction;
import org.poo.bank.entity.transaction.card.DeleteCardTransaction;
import org.poo.bank.entity.transaction.payment.InsufficientFundsTransaction;
import org.poo.bank.entity.transaction.payment.SendMoneyTransaction;
import org.poo.bank.entity.transaction.payment.splitPayment.SplitPaymentErrorTransaction;
import org.poo.bank.entity.transaction.payment.splitPayment.SplitPaymentTransaction;
import org.poo.bank.entity.transaction.withdraw.CashWithdrawalTransaction;
import org.poo.bank.entity.transaction.withdraw.WithdrawSavingsTransaction;
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

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(WithdrawSavingsTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(UpgradePlanTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(CashWithdrawalTransaction transaction);

    /**
     * Converts a transaction to an object node.
     * @param transaction The transaction.
     * @return The object node.
     */
    ObjectNode toObjectNode(AddInterestTransaction transaction);
}
