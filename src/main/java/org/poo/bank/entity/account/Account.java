package org.poo.bank.entity.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.List;
import java.util.Objects;

@Data
@Builder
public class Account implements ObjectNodeAcceptor {
    private static final Double WARNING_BALANCE_THRESHOLD = 30.0;
    private String IBAN;
    private Double balance;
    private String currency;
    private AccountType accountType;
    private List<Card> cards;
    private Double interestRate;
    private Double minimumBalance;

    public boolean subtractBalance(Double subtraction, Card card) {
        if (subtractBalance(subtraction)) {
            if (balance <= minimumBalance) {
                card.setStatus(CardStatus.CARD_STATUS_FROZEN);
            } else if (balance - minimumBalance <= WARNING_BALANCE_THRESHOLD) {
                card.setStatus(CardStatus.CARD_STATUS_WARNING);
            }

            return true;
        }

        return false;
    }

    public boolean subtractBalance(Double subtraction) {
        if (balance - subtraction >= 0) {
            balance -= subtraction;
            return true;
        }

        return false;
    }

    public void addBalance(Double addition) {
        balance += addition;
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
