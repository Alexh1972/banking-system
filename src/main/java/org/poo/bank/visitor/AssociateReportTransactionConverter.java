package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.account.Associates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AssociateReportTransactionConverter implements AssociateReportConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public ObjectNode toObjectNode(Associate associate, Integer start, Integer finish) {
        ObjectNode objectNode = MAPPER.createObjectNode();

        Double spent = BigDecimal.valueOf(associate.getSpent(start, finish)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Double deposited = BigDecimal.valueOf(associate.getDeposited(start, finish)).setScale(2, RoundingMode.HALF_UP).doubleValue();

        objectNode.put("username", associate.getUser().getFirstName() + " " + associate.getUser().getLastName());
        objectNode.put("spent", spent);
        objectNode.put("deposited", deposited);
        return objectNode;
    }

    public ArrayNode toArrayNode(List<Associate> associates, Integer start, Integer finish) {
        ArrayNode arrayNode = MAPPER.createArrayNode();
        for (Associate associate : associates) {
            arrayNode.add(toObjectNode(associate, start, finish));
        }
        return arrayNode;
    }

    @Override
    public ObjectNode toObjectNode(Associates associates, Integer start, Integer finish) {
        Account account = BankSingleton.getInstance().getAccount(associates.getIban());
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("IBAN", account.getIban());
        objectNode.put("balance", BigDecimal.valueOf(account.getBalance()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        objectNode.put("currency", account.getCurrency());
        objectNode.put("spending limit", BigDecimal.valueOf(associates.getPaymentLimit()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        objectNode.put("deposit limit", BigDecimal.valueOf(associates.getDepositLimit()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        objectNode.put("statistics type", "transaction");

        for (Associate.AssociateType type : Associate.AssociateType.values()) {
            List<Associate> associateList = new ArrayList<>();
            if (!type.equals(Associate.AssociateType.OWNER)) {
                for (Associate associate : associates.getAssociates()) {
                    if (associate.getType().equals(type)) {
                        associateList.add(associate);
                    }
                }

                associateList = associateList.stream().sorted((o1, o2) -> {
                    String first = o1.getUser().getFirstName() + " " + o1.getUser().getLastName();
                    String second = o2.getUser().getFirstName() + " " + o2.getUser().getLastName();
                    return first.compareTo(second);
                }).toList();

                objectNode.put(type.getName() + "s", toArrayNode(associateList, start, finish));
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

        objectNode.put("total spent", BigDecimal.valueOf(spent).setScale(2, RoundingMode.HALF_UP).doubleValue());
        objectNode.put("total deposited", BigDecimal.valueOf(deposited).setScale(2, RoundingMode.HALF_UP).doubleValue());
        return objectNode;
    }
}
