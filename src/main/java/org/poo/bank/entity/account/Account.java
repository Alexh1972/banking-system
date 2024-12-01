package org.poo.bank.entity.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.account.card.CardType;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransferType;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Account implements ObjectNodeAcceptor {
    private static final Double WARNING_BALANCE_THRESHOLD = 30.0;
    private String IBAN;
    private Double balance;
    private String currency;
    private AccountType accountType;
    private List<Card> cards;
    private Double interestRate;
    private Double minimumBalance;
    private List<Transaction> transactions;
    @Builder
    public Account(String IBAN, Double balance, String currency, AccountType accountType, Double interestRate, Double minimumBalance) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.cards = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public boolean canPay(Double amount) {
        return amount <= balance;
    }

    public boolean isMinimumBalanceReached() {
        return minimumBalance >= balance;
    }
    public TransferType subtractBalance(Double subtraction, Card card) {
        if (!card.getStatus().equals(CardStatus.CARD_STATUS_FROZEN)) {
            if (subtractBalance(subtraction)) {
                if (balance <= minimumBalance) {
                    card.setStatus(CardStatus.CARD_STATUS_FROZEN);
                } else if (balance - minimumBalance <= WARNING_BALANCE_THRESHOLD) {
                    card.setStatus(CardStatus.CARD_STATUS_WARNING);
                }

                return TransferType.TRANSFER_TYPE_SUCCESSFUL;
            } else {
                if (card.getType().equals(CardType.CARD_TYPE_ONE_TIME)) {
                    card.setStatus(CardStatus.CARD_STATUS_FROZEN);
                }

                return TransferType.TRANSFER_TYPE_INSUFFICIENT_FUNDS;
            }
        } else {
            return TransferType.TRANSFER_TYPE_FROZEN_CARD;
        }
    }

    public boolean subtractBalance(Double subtraction) {
        if (canPay(subtraction)) {
            balance -= subtraction;
            return true;
        }

        return false;
    }

    public void addInterest() {
        balance += interestRate * balance;
    }

    public void addBalance(Double addition) {
        balance += addition;
    }

    public void transactionUpdate(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(IBAN, account.IBAN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IBAN);
    }
}
