package org.poo.bank.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.user.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AssociateReportCommerciantConverter implements AssociateReportConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public ObjectNode toObjectNode(Associates associates, Integer start, Integer finish) {
        Account account = BankSingleton.getInstance().getAccount(associates.getIban());
        ObjectNode objectNode = MAPPER.createObjectNode();

        objectNode.put("IBAN", account.getIban());
        objectNode.put("balance", account.getBalance());
        objectNode.put("currency", account.getCurrency());
        objectNode.put("spending limit", associates.getPaymentLimit());
        objectNode.put("deposit limit", associates.getDepositLimit());
        objectNode.put("statistics type", "commerciant");

        ArrayNode arrayNode = MAPPER.createArrayNode();
        for (Commerciant commerciant : associates.getCommerciantList()) {
            arrayNode.add(toObjectNode(associates, commerciant, start, finish));
        }

        objectNode.put("commerciants", arrayNode);

        return objectNode;
    }

    public ObjectNode toObjectNode(Associates associates, Commerciant commerciant, Integer start, Integer finish) {
        Bank bank = BankSingleton.getInstance();
        ObjectNode objectNode = MAPPER.createObjectNode();

        Double total = 0.0;
        for (Associate.AssociateType type : Associate.AssociateType.values()) {
            if (type.equals(Associate.AssociateType.OWNER)) {
                continue;
            }

            List<Associate> associateList = new ArrayList<>();
            for (Associate.AssociateInformation information : associates.getCommerciantSpending(commerciant, start, finish)) {
                User user = bank.getUser(information.getUser());
                Associate associate = associates.getAssociate(user);
                if (associate.getType().equals(type)) {
                    total += information.getValue();
                    associateList.add(associate);
                }
            }

            associateList = associateList.stream().sorted((o1, o2) -> {
                String first = o1.getUser().getLastName() + " " + o1.getUser().getFirstName();
                String second = o2.getUser().getLastName() + " " + o2.getUser().getFirstName();
                return first.compareTo(second);
            }).toList();

            objectNode.put(type.getName() + "s", toArrayNode(associateList));
        }

        objectNode.put("total received", total);
        objectNode.put("commerciant", commerciant.getName());

        return objectNode;
    }


    public ArrayNode toArrayNode(List<Associate> associates) {
        ArrayNode arrayNode = MAPPER.createArrayNode();
        for (Associate associate : associates) {
            arrayNode.add(associate.getUser().getLastName() + " " + associate.getUser().getFirstName());
        }
        return arrayNode;
    }
}
