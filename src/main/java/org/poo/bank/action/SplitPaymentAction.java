package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.entity.SplitPayment;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.SplitPaymentErrorTransaction;
import org.poo.bank.entity.transaction.SplitPaymentTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
//        TransactionNotifier notifier = new TransactionNotifier();
//        List<String> accounts = commandInput.getAccounts();
//        List<Account> accountsList = new ArrayList<>();
//        boolean isPaymentValid = true;
//        String errorIBAN = null;
//        int numberPayers = accounts.size();
//
//        for (String accountIBAN : accounts) {
//            Account account = bank.getAccount(accountIBAN);
//            Double amount = bank.getAmount(
//                    commandInput.getAmount(),
//                    commandInput.getCurrency(),
//                    account.getCurrency()) / numberPayers;
//
//            if (!account.canPay(amount)) {
//                isPaymentValid = false;
//                errorIBAN = accountIBAN;
//            }
//
//            accountsList.add(account);
//            notifier.addListener(bank.getUser(account), account);
//        }
//
//        if (isPaymentValid) {
//            for (Account account : accountsList) {
//                Double amount = bank.getAmount(
//                        commandInput.getAmount(),
//                        commandInput.getCurrency(),
//                        account.getCurrency()) / numberPayers;
//
//                account.subtractBalance(amount);
//            }
//
//            Transaction transaction =
//                    new SplitPaymentTransaction(
//                    commandInput.getAmount(),
//                    commandInput.getCurrency(),
//                    numberPayers,
//                    accounts,
//                    commandInput.getTimestamp());
//            notifier.notifyUsers(transaction);
//        } else {
//            Transaction transaction =
//                    new SplitPaymentErrorTransaction(
//                    commandInput.getAmount(),
//                    commandInput.getCurrency(),
//                    numberPayers,
//                    accounts,
//                    errorIBAN,
//                    commandInput.getTimestamp());
//            notifier.notifyUsers(transaction);
//        }
//
        SplitPaymentType paymentType = SplitPaymentType.getSplitPaymentType(commandInput.getSplitPaymentType());
        List<Double> amounts = commandInput.getAmountForUsers();


        if (paymentType.equals(SplitPaymentType.EQUAL)) {
            amounts = new ArrayList<>();
            int numberPayers = commandInput.getAccounts().size();
            for (int i = 0; i < numberPayers; i++) {
                amounts.add(commandInput.getAmount() / numberPayers);
            }
        }

        SplitPayment splitPayment = new SplitPayment(commandInput.getAccounts(), amounts, commandInput.getCurrency(), paymentType.getValue(), commandInput.getTimestamp());
        bank.addSplitPayment(splitPayment);
        return null;
    }

    @Getter
    private enum SplitPaymentType {
        CUSTOM("custom"), EQUAL("equal");
        private String value;
        SplitPaymentType(String value) {
           this.value = value;
        }

        public static SplitPaymentType getSplitPaymentType(String value) {
            for (SplitPaymentType type : SplitPaymentType.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }

            return EQUAL;
        }
    }
}
