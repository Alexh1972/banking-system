package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.CardPaymentTransaction;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.entity.transaction.TransactionMessage;
import org.poo.bank.visitor.ObjectNodeConverter;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.fileio.CommandInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSpendingsReportAction extends Action {
    private static final ObjectNodeVisitor objectNodeConverter = new ObjectNodeConverter();
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null)
                throw new RuntimeException("Account not found");

            List<Transaction> transactions = account.getTransactions();
            Map<String, Double> commerciantAmountMap = new HashMap<>();

            transactions = transactions.stream()
                    .filter(t ->
                            t.getDescription()
                                    .equals(TransactionMessage
                                            .TRANSACTION_MESSAGE_CARD_PAYMENT
                                            .getValue()))
                    .filter(t ->
                            t.getTimestamp() >= commandInput.getStartTimestamp() &&
                                    t.getTimestamp() <= commandInput.getEndTimestamp())
                    .toList();

            for (Transaction transaction : transactions) {
                CardPaymentTransaction paymentTransaction = (CardPaymentTransaction) transaction;
                Double total = commerciantAmountMap.get(paymentTransaction.getCommerciant());

                if (total != null) {
                    total += paymentTransaction.getAmount();
                } else {
                    total = paymentTransaction.getAmount();
                }

                commerciantAmountMap.put(paymentTransaction.getCommerciant(), total);
            }

            ArrayNode arrayNode = getMapper().createArrayNode();
            for (String commerciant : commerciantAmountMap.keySet().stream().sorted().toList()) {
                ObjectNode objectNode = getMapper().createObjectNode();
                objectNode.put("commerciant", commerciant);
                objectNode.put("total", commerciantAmountMap.get(commerciant));
                arrayNode.add(objectNode);
            }

            ObjectNode objectNode = getMapper().createObjectNode();
            ObjectNode accountNode = getMapper().createObjectNode();
            accountNode.put("transactions", objectNodeConverter.toArrayNode(transactions));
            accountNode.put("IBAN", account.getIBAN());
            accountNode.put("balance", account.getBalance());
            accountNode.put("commerciants", arrayNode);
            accountNode.put("currency", account.getCurrency());

            objectNode.put("output", accountNode);
            return objectNode;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
