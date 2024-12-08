package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
@Setter
public class CardPaymentTransaction extends Transaction implements ObjectNodeAcceptor {
    private String commerciant;
    private Double amount;
    public CardPaymentTransaction(final String commerciant,
                                  final Double amount,
                                  final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_PAYMENT.getValue(), timestamp);
        this.commerciant = commerciant;
        this.amount = amount;
    }

    /**
     * Double dispatch for creating object node
     * representation of the transaction.
     * @param objectNodeVisitor The transaction.
     * @return The object node.
     */
    @Override
    public ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
