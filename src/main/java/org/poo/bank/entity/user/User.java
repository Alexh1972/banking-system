package org.poo.bank.entity.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.notification.TransactionListener;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.utils.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private LocalDateTime birthDate;
    private String occupation;

    public User(final String firstName,
                final String lastName,
                final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public User(final String firstName,
                final String lastName,
                final String email,
                final String birthDate,
                final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        this.birthDate = Utils.convertDate(birthDate);
        this.occupation = occupation;
    }

    public Account getFirstClassicAccount(String currency) {
        for (Account account : accounts) {
            if (account.getAccountType().equals(AccountType.ACCOUNT_TYPE_CLASSIC) &&
                    account.getCurrency().equals(currency))
                return account;
        }

        return null;
    }

    public long getAge() {
        return ChronoUnit.YEARS.between(
                LocalDate.of(birthDate.getYear(), birthDate.getMonth() , birthDate.getDayOfMonth()),
                LocalDate.of(2024 , 1 , 1)
        );
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
