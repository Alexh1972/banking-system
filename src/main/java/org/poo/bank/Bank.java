package org.poo.bank;

import lombok.Data;
import org.poo.bank.entity.ExchangeValue;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Bank {
    private List <User> users;
    private List<ExchangeValue> exchangeValues;
    private List<Transaction> transactions;
    private Map<String, Account> accountIBANMap;
    private Map<String, User> userEmailMap;
    private Map<Account, User> userAccountMap;
    private Map<String, Card> cardNumberMap;
    private Map<Card, Account> accountCardMap;

    public void initialize(ObjectInput objectInput) {
        users = new ArrayList<>();
        exchangeValues = new ArrayList<>();
        transactions = new ArrayList<>();
        accountIBANMap = new HashMap<>();
        userEmailMap = new HashMap<>();
        userAccountMap = new HashMap<>();
        cardNumberMap = new HashMap<>();
        accountCardMap = new HashMap<>();

        for (UserInput userInput : objectInput.getUsers()) {
            User user = new User(userInput.getFirstName(), userInput.getLastName(), userInput.getEmail(), new ArrayList<>());
            users.add(user);
            userEmailMap.put(userInput.getEmail(), user);
        }

        for (ExchangeInput exchangeInput : objectInput.getExchangeRates()) {
            exchangeValues.add(new ExchangeValue(exchangeInput.getTo(), exchangeInput.getFrom(), exchangeInput.getRate()));
        }
    }

    public Account getAccount(String account) {
        return accountIBANMap.get(account);
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
}
