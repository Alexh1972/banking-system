package org.poo.bank.entity.account.card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.poo.bank.entity.User;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.Objects;

@Data
@Builder
public class Card implements ObjectNodeAcceptor {
    private String cardNumber;
    private CardStatus status;
    private CardType type;
    private String ownerEmail;

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(cardNumber, card.cardNumber);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
