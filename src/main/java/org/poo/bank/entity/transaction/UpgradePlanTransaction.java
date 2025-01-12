package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
public class UpgradePlanTransaction extends Transaction {
    private String newPlanType;
    private String accountIban;
    public UpgradePlanTransaction(
            final String newPlanType,
            final String accountIban,
            final Integer timestamp
    ) {
        super(TransactionMessage.TRANSACTION_MESSAGE_UPGRADE_PLAN.getValue(), timestamp);
        this.newPlanType = newPlanType;
        this.accountIban = accountIban;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
