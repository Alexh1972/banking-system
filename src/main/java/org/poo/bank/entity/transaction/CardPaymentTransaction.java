package org.poo.bank.entity.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Account;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;
@Getter
@Setter
public class CardPaymentTransaction extends CommerciantTransaction implements ObjectNodeAcceptor {
    private Double amount;
    private String currency;
    private Double amountNoCashback;
    public CardPaymentTransaction(final String commerciantName,
                                  final Double amount,
                                  final Double amountNoCashback,
                                  final Integer timestamp,
                                  final Account account) {
        super(TransactionMessage.TRANSACTION_MESSAGE_CARD_PAYMENT.getValue(), timestamp, commerciantName, true, amount, account.getCurrency());
        this.amount = amount;
        this.amountNoCashback = amountNoCashback;
        this.currency = account.getCurrency();

        Bank bank = BankSingleton.getInstance();
        Commerciant commerciant = bank.getCommerciant(commerciantName);
        commerciant.getCashbackStrategy().updateCashback(account, commerciant);
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
