package org.poo.bank.entity.transaction.withdraw;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
public class CashWithdrawalTransaction extends Transaction {
    private Double amount;
    public CashWithdrawalTransaction(
            final Double amount,
            final Integer timestamp
    ) {
        super(TransactionMessage
                        .TRANSACTION_MESSAGE_CASH_WITHDRAW
                        .getValue()
                        .replace("{amount}", amount.toString()),
                timestamp);
        this.amount = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
