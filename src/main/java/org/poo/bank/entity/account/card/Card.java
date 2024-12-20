package org.poo.bank.entity.account.card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
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

    /**
     * Double dispatch for object node visitor.
     * @param objectNodeVisitor The object node visitor.
     * @return The object node representation of the card.
     */
    @Override
    public ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }

    /**
     * Checks if object is equal to card.
     * @param o The object.
     * @return If object is equal to card.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return Objects.equals(cardNumber, card.cardNumber);
    }

    /**
     *
     * @return Card in the string form.
     */
    @Override
    public String toString() {
        return "Card{"
                + "cardNumber='" + cardNumber + '\''
                + ", status=" + status
                + ", type=" + type
                + '}';
    }
}
