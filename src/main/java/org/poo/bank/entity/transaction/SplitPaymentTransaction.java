package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.List;

@Getter
@Setter
public class SplitPaymentTransaction extends Transaction implements ObjectNodeAcceptor {
    private Double amount;
    private String currency;
    private Integer numberPayers;
    private List<String> involvedAccounts;
    private String type;
    private List<Double> amounts;
    public SplitPaymentTransaction(final Double amount,
                                   final String currency,
                                   final Integer numberPayers,
                                   final List<String> involvedAccounts,
                                   final Integer timestamp,
                                   final String type,
                                   final List<Double> amounts) {
        super(TransactionMessage.TRANSACTION_MESSAGE_SPLIT_PAYMENT
                .getValue()
                .replace("{amount|currency}",
                        String.format("%.2f", amount) + " " + currency),
                timestamp);
        this.amount = amount;
        this.currency = currency;
        this.numberPayers = numberPayers;
        this.involvedAccounts = involvedAccounts;
        this.type = type;
        this.amounts = amounts;
    }

    public SplitPaymentTransaction(final Double amount,
                                   final String currency,
                                   final Integer numberPayers,
                                   final List<String> involvedAccounts,
                                   final String description,
                                   final Integer timestamp) {
        super(description,
                timestamp);
        this.amount = amount / numberPayers;
        this.currency = currency;
        this.numberPayers = numberPayers;
        this.involvedAccounts = involvedAccounts;
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
