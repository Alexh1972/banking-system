package org.poo.bank.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.poo.bank.entity.account.Account;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.List;

@Data
@AllArgsConstructor
public class User implements ObjectNodeAcceptor {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }
}
