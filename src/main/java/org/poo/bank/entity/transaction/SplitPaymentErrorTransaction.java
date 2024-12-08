package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.List;
@Getter
@Setter
public class SplitPaymentErrorTransaction
        extends SplitPaymentTransaction
        implements ObjectNodeAcceptor {
    private String error;
    public SplitPaymentErrorTransaction(final Double amount,
                                        final String currency,
                                        final Integer numberPayers,
                                        final List<String> involvedAccounts,
                                        final String errorIBAN,
                                        final Integer timestamp) {
        super(amount,
                currency,
                numberPayers,
                involvedAccounts,
                timestamp);

        this.error = TransactionMessage.TRANSACTION_MESSAGE_SPLIT_PAYMENT_ERROR
                .getValue()
                .replace("{IBAN}", errorIBAN);
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
