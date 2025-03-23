package org.poo.bank.entity.transaction.card;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.visitor.ObjectNodeVisitor;

@Setter
@Getter
public class DeleteCardTransaction extends Transaction {
    private String account;
    private String card;
    private String cardHolder;
    public DeleteCardTransaction(final String account,
                                 final String card,
                                 final String cardHolder,
                                 final Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_DELETED
                .getValue(),
                timestamp);
        this.account = account;
        this.card = card;
        this.cardHolder = cardHolder;
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
