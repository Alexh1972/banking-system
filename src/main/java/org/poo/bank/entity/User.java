package org.poo.bank.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.notification.TransactionListener;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class User implements ObjectNodeAcceptor, TransactionListener {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;
    private List<Transaction> transactions;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    @Override
    public ObjectNode accept(ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }

    @Override
    public void transactionUpdate(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

}
