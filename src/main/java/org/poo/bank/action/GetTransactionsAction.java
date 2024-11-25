package org.poo.bank.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.User;
import org.poo.bank.entity.transaction.Transaction;
import org.poo.bank.visitor.ObjectNodeConverter;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class GetTransactionsAction extends Action {
    private static final ObjectNodeVisitor objectNodeConverter = new ObjectNodeConverter();
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null)
                throw new RuntimeException("User not found");

            User user = bank.getUser(commandInput.getEmail());

            if (user == null)
                throw new RuntimeException("User not found");

            ObjectMapper mapper = getMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            ArrayNode arrayNode = mapper.createArrayNode();


            for (Transaction transaction : user.getTransactions()) {
                arrayNode.add(transaction.accept(objectNodeConverter));
            }

            objectNode.put("output", arrayNode);
            return objectNode;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
