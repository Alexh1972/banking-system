package org.poo.bank.entity.transaction.withdraw;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
public class WithdrawSavingsTransaction extends Transaction {
    private String classicAccountIBAN;
    private String savingsAccountIBAN;
    private Double amount;
    public WithdrawSavingsTransaction(
            final String classicAccountIBAN,
            final String savingsAccountIBAN,
            final Double amount,
            final Integer timestamp
    ) {
        super(TransactionMessage.TRANSACTION_MESSAGE_WITHDRAW_SAVINGS.getValue(), timestamp);
        this.classicAccountIBAN = classicAccountIBAN;
        this.savingsAccountIBAN = savingsAccountIBAN;
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
