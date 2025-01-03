package org.poo.bank.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.user.User;
import org.poo.bank.visitor.ObjectNodeConverter;
import org.poo.bank.visitor.ObjectNodeVisitor;
import org.poo.fileio.CommandInput;

public class GetUsersAction extends Action {
    private static final ObjectNodeVisitor OBJECT_NODE_CONVERTER = new ObjectNodeConverter();
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        ObjectMapper mapper = getMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (User user : bank.getUsers()) {
            arrayNode.add(user.accept(OBJECT_NODE_CONVERTER));
        }

        objectNode.put("output", arrayNode);
        return objectNode;
    }
}
