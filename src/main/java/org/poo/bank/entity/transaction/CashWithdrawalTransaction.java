package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
public class CashWithdrawalTransaction extends Transaction {
    private Double amount;
    public CashWithdrawalTransaction(Double amount, Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CASH_WITHDRAW.getValue().replace("{amount}", amount.toString()), timestamp);
        this.amount = amount;
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
