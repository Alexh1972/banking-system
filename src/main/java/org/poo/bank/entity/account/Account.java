package org.poo.bank.entity.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransferType;
import org.poo.bank.entity.user.User;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Data
public class Account implements ObjectNodeAcceptor {
    private static final Double WARNING_BALANCE_THRESHOLD = 30.0;
    private String iban;
    private Double balance;
    private String currency;
    private AccountType accountType;
    private List<Card> cards;
    private Double interestRate;
    private Double minimumBalance;
    private List<Transaction> transactions;
    private ServicePlan servicePlan;
    private List<Card> usedOneTimeCards;
    private Integer highSumPayments = 0;
    @Builder
    public Account(final String iban,
                   final Double balance,
                   final String currency,
                   final AccountType accountType,
                   final Double interestRate,
                   final Double minimumBalance,
                   final ServicePlan servicePlan) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.cards = new ArrayList<>();
        this.transactions = new ArrayList<>();

        this.servicePlan = servicePlan;
    }

    /**
     * Checks if the account can pay the amount.
     * @param amount The amount.
     * @return If the account can pay;
     */
    public final boolean canPay(final Double amount) {
        return amount <= balance;
    }

    public final boolean canUpgradePlan(final Double amount) {
        if (!servicePlan.equals(ServicePlan.SILVER)) {
            return false;
        }

        if (highSumPayments == 5) {
            return true;
        }
        Bank bank = BankSingleton.getInstance();
        Double amountConverted = bank.getAmount(amount, getCurrency(), "RON");

        if (amountConverted >= 300.0) {
            highSumPayments++;
            return highSumPayments == 5;
        }

        return false;
    }

    /**
     * Checks if the minimum balanced is reached.
     * @return If the minimum balanced is reached.
     */
    public final boolean isMinimumBalanceReached() {
        return minimumBalance >= balance;
    }

    /**
     * Subtract the amount of money from balance after
     * a card payment.
     * @param subtraction The amount of money.
     * @param card The card.
     * @return The result of the payment.
     */
    public final TransferType subtractBalance(final Double subtraction,
                                              final Card card) {
        if (!card.getStatus().equals(CardStatus.CARD_STATUS_FROZEN)) {
            if (subtractBalance(subtraction)) {
                if (balance <= minimumBalance) {
                    card.setStatus(CardStatus.CARD_STATUS_FROZEN);
                }

                return TransferType.TRANSFER_TYPE_SUCCESSFUL;
            } else {
                return TransferType.TRANSFER_TYPE_INSUFFICIENT_FUNDS;
            }
        } else {
            return TransferType.TRANSFER_TYPE_FROZEN_CARD;
        }
    }

    /**
     * Subtract money from balance.
     * @param subtraction The amount of money.
     * @return If the subtraction could be done.
     */
    public final boolean subtractBalance(final Double subtraction) {
        if (canPay(subtraction)) {
            balance -= subtraction;
            return true;
        }

        return false;
    }

    /**
     * Adds the interest.
     */
    public final void addInterest() {
        balance += getInterestAmount();
    }

    public final Double getInterestAmount() {
        return interestRate * balance;
    }

    /**
     * Adds funds to an account.
     * @param addition The amount.
     */
    public final void addBalance(final Double addition) {
        balance += addition;
    }

    /**
     * Notifies the account that a transaction was made.
     * @param transaction The transaction.
     */
    public final void transactionUpdate(final Transaction transaction) {
        transactions.add(transaction);
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
    }

    @Override
    public final ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }

    /**
     * Checks if objects is equal to account.
     * @param o The object.
     * @return If object is equal to account.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(iban, account.iban);
    }

    /**
     *
     * @return Hash code of account.
     */
    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }
}
