package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeVisitor;

@Setter
@Getter
public class SendMoneyTransaction extends Transaction {
    private String amount;
    private String receiverIBAN;
    private String senderIBAN;
    private TransferType transferType;

    public SendMoneyTransaction(final String description,
                                final Integer timestamp,
                                final Double amount,
                                final String currency,
                                final String receiverIBAN,
                                final String senderIBAN,
                                final TransferType transferType) {
        super(description, timestamp);
        this.amount = amount + " " + currency;
        this.receiverIBAN = receiverIBAN;
        this.senderIBAN = senderIBAN;
        this.transferType = transferType;
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
