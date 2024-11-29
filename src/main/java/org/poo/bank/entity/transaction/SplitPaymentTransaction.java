package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@Setter
public class SplitPaymentTransaction extends Transaction implements ObjectNodeAcceptor {
    private Double amount;
    private String currency;
    private Integer numberPayers;
    private List<String> involvedAccounts;
    public SplitPaymentTransaction(Double amount,
                                   String currency,
                                   Integer numberPayers,
                                   List<String> involvedAccounts,
                                   Integer timestamp) {
        super(TransactionMessage.TRANSACTION_MESSAGE_SPLIT_PAYMENT
                .getValue()
                .replace("{amount|currency}",
                        String.format("%.2f", amount) + " " + currency),
                timestamp);
        this.amount = amount / numberPayers;
        this.currency = currency;
        this.numberPayers = numberPayers;
        this.involvedAccounts = involvedAccounts;
    }

    public SplitPaymentTransaction(Double amount,
                                   String currency,
                                   Integer numberPayers,
                                   List<String> involvedAccounts,
                                   String description,
                                   Integer timestamp) {
        super(description,
                timestamp);
        this.amount = amount / numberPayers;
        this.currency = currency;
        this.numberPayers = numberPayers;
        this.involvedAccounts = involvedAccounts;
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
