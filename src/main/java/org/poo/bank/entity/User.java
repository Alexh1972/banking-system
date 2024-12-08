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

    public User(final String firstName,
                final String lastName,
                final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    /**
     * Double dispatch for converting user to object node.
     * @param objectNodeVisitor The object node visitor.
     * @return The object node.
     */
    @Override
    public ObjectNode accept(final ObjectNodeVisitor objectNodeVisitor) {
        return objectNodeVisitor.toObjectNode(this);
    }

    /**
     * Notifies the user after receiving a transaction.
     * @param transaction The transaction.
     */
    @Override
    public void transactionUpdate(final Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    /**
     *
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

}
