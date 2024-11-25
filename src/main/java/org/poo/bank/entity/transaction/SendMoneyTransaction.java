package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
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

    public SendMoneyTransaction(String description, Integer timestamp, Double amount, String currency, String receiverIBAN, String senderIBAN, TransferType transferType) {
        super(description, timestamp);
        this.amount = amount + " " + currency;
        this.receiverIBAN = receiverIBAN;
        this.senderIBAN = senderIBAN;
        this.transferType = transferType;
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
