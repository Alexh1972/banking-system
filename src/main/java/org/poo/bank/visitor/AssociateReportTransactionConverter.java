package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.account.Associates;

import java.util.ArrayList;
import java.util.List;

public class AssociateReportTransactionConverter implements AssociateReportConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ObjectNode toObjectNode(
            final Associate associate,
            final Integer start,
            final Integer finish
    ) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        Double spent = associate.getSpent(start, finish);
        Double deposited = associate.getDeposited(start, finish);

        objectNode.put(
                "username",
                associate.getUser().getLastName()
                        + " "
                        + associate.getUser().getFirstName());
        objectNode.put("spent", spent);
        objectNode.put("deposited", deposited);
        return objectNode;
    }

    private ArrayNode toArrayNode(
            final List<Associate> associates,
            final Integer start,
            final Integer finish
    ) {
        ArrayNode arrayNode = MAPPER.createArrayNode();
        for (Associate associate : associates) {
            arrayNode.add(toObjectNode(associate, start, finish));
        }
        return arrayNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode toObjectNode(
            final Associates associates,
            final Integer start,
            final Integer finish
    ) {
        Account account = BankSingleton.getInstance()
                .getAccount(associates.getIban());
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("IBAN", account.getIban());
        objectNode.put("balance", account.getBalance());
        objectNode.put("currency", account.getCurrency());
        objectNode.put("spending limit", associates.getPaymentLimit());
        objectNode.put("deposit limit", associates.getDepositLimit());
        objectNode.put("statistics type", "transaction");

        for (Associate.AssociateType type : Associate.AssociateType.values()) {
            List<Associate> associateList = new ArrayList<>();
            if (!type.equals(Associate.AssociateType.OWNER)) {
                for (Associate associate : associates.getAssociates()) {
                    if (associate.getType().equals(type)) {
                        associateList.add(associate);
                    }
                }

                objectNode.put(
                        type.getName()
                                + "s",
                        toArrayNode(
                                associateList,
                                start,
                                finish
                        )
                );
            }
        }

        Double spent = 0.0;
        Double deposited = 0.0;
        for (Associate associate : associates.getAssociates()) {
            if (!associate.getType().equals(Associate.AssociateType.OWNER)) {
                spent += associate.getSpent(start, finish);
                deposited += associate.getDeposited(start, finish);
            }
        }

        objectNode.put("total spent", spent);
        objectNode.put("total deposited", deposited);
        return objectNode;
    }
}
