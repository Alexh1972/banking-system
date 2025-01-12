package org.poo.bank.entity.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.account.ServicePlan;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.notification.TransactionListener;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.utils.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private ServicePlan servicePlan;

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

        if (occupation.equals(ServicePlan.STUDENT.getName())) {
            servicePlan = ServicePlan.STUDENT;
        } else {
            servicePlan = ServicePlan.STANDARD;
        }
    }

    /**
     * Gets the first classic account of the user with
     * a specific currency.
     * @param currency The currency.
     * @return The account.
     */
    public final Account getFirstClassicAccount(final String currency) {
        for (Account account : accounts) {
            if (account.getAccountType().equals(AccountType.ACCOUNT_TYPE_CLASSIC)
                    && account.getCurrency().equals(currency)) {
                return account;
            }
        }

        return null;
    }

    /**
     * Gets the age of the user.
     * @return The age of the user.
     */
    public final long getAge() {
        Calendar calendar = Calendar.getInstance();
        return ChronoUnit.YEARS.between(
                LocalDate.of(birthDate.getYear(), birthDate.getMonth(), birthDate.getDayOfMonth()),
                LocalDate.of(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DATE)
                )
        );
    }

    public final void setPlan(final ServicePlan newServicePlan) {
        servicePlan = newServicePlan;
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
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
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
