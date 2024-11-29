package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.List;
@Getter
@Setter
public class SplitPaymentErrorTransaction extends SplitPaymentTransaction implements ObjectNodeAcceptor {
    private String error;
    public SplitPaymentErrorTransaction(Double amount, String currency, Integer numberPayers, List<String> involvedAccounts, String errorIBAN, Integer timestamp) {
        super(amount,
                currency,
                numberPayers,
                involvedAccounts,
                timestamp);

        this.error = TransactionMessage.TRANSACTION_MESSAGE_SPLIT_PAYMENT_ERROR.getValue().replace("{IBAN}", errorIBAN);
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
