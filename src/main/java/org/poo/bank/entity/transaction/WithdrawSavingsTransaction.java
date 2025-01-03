package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.Getter;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
public class WithdrawSavingsTransaction extends Transaction {
    private String classicAccountIBAN;
    private String savingsAccountIBAN;
    private Double amount;
    public WithdrawSavingsTransaction(String classicAccountIBAN, String savingsAccountIBAN, Double amount, Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_WITHDRAW_SAVINGS.getValue(), timestamp);
        this.classicAccountIBAN = classicAccountIBAN;
        this.savingsAccountIBAN = savingsAccountIBAN;
        this.amount = amount;
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
