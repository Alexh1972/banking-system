package org.poo.bank.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.user.User;
import org.poo.bank.visitor.ObjectNodeConverter;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.fileio.CommandInput;

public class GetTransactionsAction extends Action {
    private static final ObjectNodeVisitor OBJECT_NODE_CONVERTER = new ObjectNodeConverter();
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            if (commandInput.getEmail() == null) {
                throw new RuntimeException("User not found");
            }

            User user = bank.getUser(commandInput.getEmail());

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            ObjectMapper mapper = getMapper();
            ObjectNode objectNode = mapper.createObjectNode();

            objectNode.put("output",
                    OBJECT_NODE_CONVERTER.toArrayNode(
                            user.getTransactions()));
            return objectNode;
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }
    }
}
