package org.poo.bank;

import lombok.Data;
import org.poo.bank.entity.ExchangeValue;
import org.poo.bank.entity.Transaction;
import org.poo.bank.entity.User;
import org.poo.bank.entity.account.Account;
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

    public void initialize(ObjectInput objectInput) {
        users = new ArrayList<>();
        exchangeValues = new ArrayList<>();
        transactions = new ArrayList<>();
        accountIBANMap = new HashMap<>();

        for (UserInput userInput : objectInput.getUsers()) {
            users.add(new User(userInput.getFirstName(), userInput.getLastName(), userInput.getEmail(), new ArrayList<>()));
        }

        for (ExchangeInput exchangeInput : objectInput.getExchangeRates()) {
            exchangeValues.add(new ExchangeValue(exchangeInput.getTo(), exchangeInput.getFrom(), exchangeInput.getRate()));
        }
    }
}
