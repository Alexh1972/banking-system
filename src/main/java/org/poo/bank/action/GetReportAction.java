package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.visitor.ObjectNodeConverter;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.fileio.CommandInput;

import java.util.List;

public class GetReportAction extends Action {
    private static final ObjectNodeVisitor OBJECT_NODE_CONVERTER = new ObjectNodeConverter();
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                throw new RuntimeException("Account not found");
            }

            List<Transaction> transactions = account.getTransactions();

            transactions = transactions.stream()
                    .filter(t ->
                            t.getTimestamp() >= commandInput.getStartTimestamp()
                                    && t.getTimestamp() <= commandInput.getEndTimestamp())
                    .toList();

            ObjectNode objectNode = getMapper().createObjectNode();
            ObjectNode accountNode = getMapper().createObjectNode();
            accountNode.put("transactions", OBJECT_NODE_CONVERTER.toArrayNode(transactions));
            accountNode.put("IBAN", account.getIban());
            accountNode.put("balance", account.getBalance());
            accountNode.put("currency", account.getCurrency());

            objectNode.put("output", accountNode);
            return objectNode;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
