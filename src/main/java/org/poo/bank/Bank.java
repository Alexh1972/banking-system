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
    private List <User> users;
    private Map<String, Account> accountIBANMap;
    private Map<String, User> userEmailMap;
    private Map<Account, User> userAccountMap;
    private Map<String, Card> cardNumberMap;
    private Map<Card, Account> accountCardMap;
    private Map<String, BigDecimal> exchangeRates;
    private Map<Alias, String> aliasMap;
    private Map<String, String> globalAliasMap;

    public void initialize(ObjectInput objectInput) {
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
            User user = new User(userInput.getFirstName(), userInput.getLastName(), userInput.getEmail());
            users.add(user);
            userEmailMap.put(userInput.getEmail(), user);
        }

        List<ExchangeRate> rates = new ArrayList<>();
        for (ExchangeInput exchangeInput : objectInput.getExchangeRates()) {
            rates.add(new ExchangeRate(exchangeInput.getFrom(), exchangeInput.getTo(), BigDecimal.valueOf(exchangeInput.getRate())));
            rates.add(new ExchangeRate(exchangeInput.getTo(), exchangeInput.getFrom(), BigDecimal.valueOf(1 / exchangeInput.getRate())));
        }

        combineRates(rates);
    }

    private void combineRates(List<ExchangeRate> rates) {
        for (ExchangeRate exchangeRate : rates) {
            exchangeRates.put(exchangeRate.getFrom() + DELIMITER + exchangeRate.getTo(), exchangeRate.getRate());
        }

        boolean changed = true;
        while (changed) {
            changed = false;

            for (int i = 0; i < rates.size(); i++) {
                for (int j = i + 1; j < rates.size(); j++) {
                    ExchangeRate first = rates.get(i);
                    ExchangeRate second = rates.get(j);

                    if (first.getTo().equals(second.getFrom()) && !first.getFrom().equals(second.getTo())) {
                        if (!exchangeRates.containsKey(first.getFrom() + DELIMITER + second.getTo())) {
                            changed = true;

                            exchangeRates.put(first.getFrom() + DELIMITER + second.getTo(), first.getRate().multiply(second.getRate()));
                            ExchangeRate exchangeRate = new ExchangeRate(first.getFrom(), second.getTo(), first.getRate().multiply(second.getRate()));
                            rates.add(exchangeRate);
                        }
                    }

                    if (second.getTo().equals(first.getFrom()) && !second.getFrom().equals(first.getTo())) {
                        if (!exchangeRates.containsKey(second.getFrom() + DELIMITER + first.getTo())) {
                            changed = true;

                            exchangeRates.put(second.getFrom() + DELIMITER + first.getTo(), first.getRate().multiply(second.getRate()));
                            ExchangeRate exchangeRate = new ExchangeRate(second.getFrom(), first.getTo(), first.getRate().multiply(second.getRate()));
                            rates.add(exchangeRate);
                        }
                    }

                }
            }
        }
    }

    public Account getAccount(String account) {
        return accountIBANMap.get(account);
    }

    public Account getAccount(String email, String aliasName, boolean global) {
        String account;
        if (!global) {
            Alias alias = new Alias(aliasName, email);
            account = aliasMap.get(alias);
        } else {
            account = globalAliasMap.get(aliasName);
        }

        if (account == null)
            account = aliasName;

        return getAccount(account);
    }

    public Account getAlias(String alias) {
        String account = globalAliasMap.get(alias);

        if (account == null)
            account = alias;

        return getAccount(account);
    }
    public void setAlias(String user, String aliasName, String IBAN) {
        Alias alias = new Alias(aliasName, user);
        aliasMap.put(alias, IBAN);
    }

    public void setAlias(String aliasName, String IBAN) {
        globalAliasMap.put(aliasName, IBAN);
    }

    public User getUser(String email) {
        return userEmailMap.get(email);
    }

    public void addAccount(Account account, User user) {
        user.getAccounts().add(account);
        accountIBANMap.put(account.getIBAN(), account);
        userAccountMap.put(account, user);
    }

    public void addCard(Account account, Card card) {
        if (account.getCards() != null) {
            account.getCards().add(card);
            cardNumberMap.put(card.getCardNumber(), card);
            accountCardMap.put(card, account);
        }
    }

    public User getUser(Account account) {
        return userAccountMap.get(account);
    }

    public void deleteAccount(User user, Account account) {
        for (Card card : account.getCards()) {
            deleteCard(card, account);
        }

        user.getAccounts().remove(account);
        accountIBANMap.remove(account.getIBAN());
        userAccountMap.remove(account);
    }

    public void deleteCard(Card card, Account account) {
        account.getCards().remove(card);
        cardNumberMap.remove(card.getCardNumber());
        accountCardMap.remove(card);
    }

    public Card getCard(String card) {
        return cardNumberMap.get(card);
    }

    public Account getAccount(Card card) {
        return accountCardMap.get(card);
    }

    public User getCardUser(Card card) {
        Account account = getAccount(card);
        return getUser(account);
    }

    public Double getRate(String from, String to) {
        return exchangeRates.get(from + DELIMITER + to).doubleValue();
    }

    public Double getAmount(Double amount, String from, String to) {
        if (amount == null)
            return null;

        if (to.equals(from))
            return amount;

        Double rate = getRate(from, to);

        if (rate != null) {
            return amount * rate;
        }

        return null;
    }
}
