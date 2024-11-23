package org.poo.bank.entity.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.List;

@Data
@Builder
public class Account implements ObjectNodeAcceptor {
    private String IBAN;
    private Double balance;
    private String currency;
    private AccountType accountType;
    private List<Card> cards;
    private Double interestRate;
    private Double minimumBalance;

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
