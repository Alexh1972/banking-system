package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.transaction.BaseTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.entity.transaction.WithdrawSavingsTransaction;
import org.poo.bank.entity.user.User;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class WithdrawSavingsAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                throw new RuntimeException("Account not found");
            }

            User user = bank.getUser(account);

            if (user.getAge() < 21) {
                TransactionNotifier.notify(new BaseTransaction(TransactionMessage.TRANSACTION_MESSAGE_WITHDRAW_SAVINGS_AGE.getValue(),
                        commandInput.getTimestamp()),
                        user,
                        account);
                return null;
            }

            Account classicAccount = user.getFirstClassicAccount(commandInput.getCurrency());

            if (classicAccount == null) {
                TransactionNotifier.notify(new BaseTransaction(TransactionMessage.TRANSACTION_MESSAGE_WITHDRAW_SAVINGS_CLASSIC_ACCOUNT.getValue(),
                                commandInput.getTimestamp()),
                        user,
                        account);
                return null;
            }

            if (!account.getAccountType().equals(AccountType.ACCOUNT_TYPE_SAVINGS)) {
                TransactionNotifier.notify(new BaseTransaction(TransactionMessage.TRANSACTION_MESSAGE_WITHDRAW_SAVINGS_SAVINGS_ACCOUNT.getValue(),
                                commandInput.getTimestamp()),
                        user,
                        account);
                return null;
            }

            Double amountReceived = commandInput.getAmount();
            Double amountSent = bank.getAmount(amountReceived,
                    classicAccount.getCurrency(),
                    account.getCurrency());

//            amountSent += account.getServicePlan().getCommission(amountSent, account.getCurrency());

            if (account.subtractBalance(amountSent)) {
                classicAccount.addBalance(amountReceived);
                TransactionNotifier.notify(new WithdrawSavingsTransaction(
                                classicAccount.getIban(),
                                account.getIban(),
                                commandInput.getAmount(),
                                commandInput.getTimestamp()),
                        user,
                        account);
                TransactionNotifier.notify(new WithdrawSavingsTransaction(
                                classicAccount.getIban(),
                                account.getIban(),
                                commandInput.getAmount(),
                                commandInput.getTimestamp()),
                        user,
                        classicAccount);
            } else {
                TransactionNotifier.notify(new BaseTransaction(TransactionMessage.TRANSACTION_MESSAGE_INSUFFICIENT_FUNDS.getValue(),
                                commandInput.getTimestamp()),
                        user,
                        account);
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
        return null;
    }
}
