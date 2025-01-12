package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
public class AddInterestTransaction extends Transaction {
    private Double amount;
    private String currency;
    public AddInterestTransaction(
            final Double amount,
            final String currency,
            final Integer timestamp
    ) {
        super(TransactionMessage.TRANSACTION_MESSAGE_ADD_INTEREST.getValue(), timestamp);
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
