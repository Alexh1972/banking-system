package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.poo.bank.entity.Alias;
import org.poo.bank.entity.ExchangeRate;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Bank {
    private static final String DELIMITER = "$";
    private List<User> users;
    private Map<String, Account> accountIBANMap;
    private Map<String, User> userEmailMap;
    private Map<Account, User> userAccountMap;
    private Map<String, Card> cardNumberMap;
    private Map<Card, Account> accountCardMap;
    private Map<String, BigDecimal> exchangeRates;
    private Map<Alias, String> aliasMap;
    private Map<String, String> globalAliasMap;

    /**
     * Initialize the bank given input such as exchange rates,
     * users, etc.
     * @param objectInput The input.
     */
    public final void initialize(final ObjectInput objectInput) {
        users = new ArrayList<>();
        accountIBANMap = new HashMap<>();
        userEmailMap = new HashMap<>();
        userAccountMap = new HashMap<>();
        cardNumberMap = new HashMap<>();
        accountCardMap = new HashMap<>();
        exchangeRates = new HashMap<>();
        aliasMap = new HashMap<>();
        globalAliasMap = new HashMap<>();

        for (UserInput userInput : objectInput.getUsers()) {
            User user = new User(
                    userInput.getFirstName(),
                    userInput.getLastName(),
                    userInput.getEmail());
            users.add(user);
            userEmailMap.put(userInput.getEmail(), user);
        }

        List<ExchangeRate> rates = new ArrayList<>();
        for (ExchangeInput exchangeInput : objectInput.getExchangeRates()) {
            rates.add(
                    new ExchangeRate(
                            exchangeInput.getFrom(),
                            exchangeInput.getTo(),
                            BigDecimal.valueOf(exchangeInput.getRate())));
            rates.add(new ExchangeRate(
                    exchangeInput.getTo(),
                    exchangeInput.getFrom(),
                    BigDecimal.valueOf(1 / exchangeInput.getRate())));
        }

        combineRates(rates);
    }

    private void combineRates(final List<ExchangeRate> rates) {
        for (ExchangeRate exchangeRate : rates) {
            exchangeRates.put(
                    exchangeRate.getFrom()
                            + DELIMITER
                            + exchangeRate.getTo(),
                    exchangeRate.getRate());
        }

        boolean changed = true;
        while (changed) {
            changed = false;

            for (int i = 0; i < rates.size(); i++) {
                for (int j = i + 1; j < rates.size(); j++) {
                    ExchangeRate first = rates.get(i);
                    ExchangeRate second = rates.get(j);

                    if (first.getTo().equals(second.getFrom())
                            && !first.getFrom().equals(second.getTo())) {
                        if (!exchangeRates.containsKey(
                                first.getFrom() + DELIMITER + second.getTo())) {
                            changed = true;

                            exchangeRates.put(
                                    first.getFrom() + DELIMITER + second.getTo(),
                                    first.getRate().multiply(second.getRate()));
                            ExchangeRate exchangeRate =
                                    new ExchangeRate(
                                            first.getFrom(),
                                            second.getTo(),
                                            first.getRate().multiply(second.getRate()));
                            rates.add(exchangeRate);
                        }
                    }

                    if (second.getTo().equals(first.getFrom())
                            && !second.getFrom().equals(first.getTo())) {
                        if (!exchangeRates.containsKey(
                                second.getFrom() + DELIMITER + first.getTo())) {
                            changed = true;

                            exchangeRates.put(
                                    second.getFrom() + DELIMITER + first.getTo(),
                                    first.getRate().multiply(second.getRate()));
                            ExchangeRate exchangeRate =
                                    new ExchangeRate(second.getFrom(),
                                            first.getTo(),
                                            first.getRate().multiply(second.getRate()));
                            rates.add(exchangeRate);
                        }
                    }

                }
            }
        }
    }

    /**
     * Get account by IBAN.
     * @param account The IBAN.
     * @return The account.
     */
    public final Account getAccount(final String account) {
        return accountIBANMap.get(account);
    }

    /**
     * Get account by alias, if exists, or by IBAN.
     * @param email The email of the user which set the alias.
     * @param aliasName The alias, if exists, or the IBAN.
     * @param global If alias is global.
     * @return The account.
     */
    public final Account getAccount(final String email,
                                    final String aliasName,
                                    final boolean global) {
        String account;
        if (!global) {
            Alias alias = new Alias(aliasName, email);
            account = aliasMap.get(alias);
        } else {
            account = globalAliasMap.get(aliasName);
        }

        if (account == null) {
            account = aliasName;
        }

        return getAccount(account);
    }

    /**
     * Get global alias.
     * @param alias The alias.
     * @return The account.
     */
    public final Account getAlias(final String alias) {
        String account = globalAliasMap.get(alias);

        if (account == null) {
            account = alias;
        }

        return getAccount(account);
    }

    /**
     * Set personal alias.
     * @param user The user which sets the alias.
     * @param aliasName The alias.
     * @param iban The iban.
     */
    public final void setAlias(final String user,
                               final String aliasName,
                               final String iban) {
        Alias alias = new Alias(aliasName, user);
        aliasMap.put(alias, iban);
    }

    /**
     * Set global alias.
     * @param aliasName The alias.
     * @param iban The iban.
     */
    public final void setAlias(final String aliasName,
                         final String iban) {
        globalAliasMap.put(aliasName, iban);
    }

    /**
     * Get user by email.
     * @param email The email.
     * @return The user.
     */
    public final User getUser(final String email) {
        return userEmailMap.get(email);
    }

    /**
     * Add account to user.
     * @param account The account.
     * @param user The user.
     */
    public final void addAccount(final Account account,
                                 final User user) {
        user.getAccounts().add(account);
        accountIBANMap.put(account.getIban(), account);
        userAccountMap.put(account, user);
    }

    /**
     * Add card to account.
     * @param account The account.
     * @param card The user.
     */
    public final void addCard(final Account account,
                              final Card card) {
        if (account.getCards() != null) {
            account.getCards().add(card);
            cardNumberMap.put(card.getCardNumber(), card);
            accountCardMap.put(card, account);
        }
    }

    /**
     * Get user of the account.
     * @param account The account.
     * @return The user.
     */
    public final User getUser(final Account account) {
        return userAccountMap.get(account);
    }

    /**
     * Delete account of a user.
     * @param user The user.
     * @param account The account.
     */
    public final void deleteAccount(final User user,
                              final Account account) {
        for (Card card : account.getCards()) {
            deleteCard(card, account);
        }

        user.getAccounts().remove(account);
        accountIBANMap.remove(account.getIban());
        userAccountMap.remove(account);
    }

    /**
     * Delete card of an account.
     * @param card The card.
     * @param account The account.
     */
    public final void deleteCard(final Card card, final Account account) {
        account.getCards().remove(card);
        cardNumberMap.remove(card.getCardNumber());
        accountCardMap.remove(card);
    }

    /**
     * Get card by card number.
     * @param card The card number.
     * @return The card.
     */
    public final Card getCard(final String card) {
        return cardNumberMap.get(card);
    }

    /**
     * Get the account which owns a card.
     * @param card The card.
     * @return The account.
     */
    public final Account getAccount(final Card card) {
        return accountCardMap.get(card);
    }

    /**
     * Get the user which owns a card.
     * @param card The card.
     * @return The user.
     */
    public final User getCardUser(final Card card) {
        Account account = getAccount(card);
        return getUser(account);
    }

    /**
     * Get the rate from a currency to another.
     * @param from From currency.
     * @param to To currency.
     * @return The rate.
     */
    public final Double getRate(final String from,
                                final String to) {
        return exchangeRates
                .get(from + DELIMITER + to).doubleValue();
    }

    /**
     * Get the amount transformed from a currency to
     * another.
     * @param amount The amount.
     * @param from From currency;
     * @param to To currency;
     * @return The amount.
     */
    public final Double getAmount(final Double amount,
                                  final String from,
                                  final String to) {
        if (amount == null) {
            return null;
        }

        if (to.equals(from)) {
            return amount;
        }

        Double rate = getRate(from, to);

        if (rate != null) {
            return amount * rate;
        }

        return null;
    }
}
