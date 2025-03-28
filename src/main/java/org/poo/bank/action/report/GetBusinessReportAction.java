package org.poo.bank.action.report;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.visitor.*;
import org.poo.fileio.CommandInput;

public class GetBusinessReportAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        AssociateReportConverter objectNodeConverter = null;

        if (commandInput.getType().equals("transaction")) {
            objectNodeConverter = new AssociateReportTransactionConverter();
        } else {
            objectNodeConverter = new AssociateReportCommerciantConverter();
        }

        Account account = bank.getAccount(commandInput.getAccount());
        Associates associates = bank.getAssociates(account);
        ObjectNode objectNode = getMapper().createObjectNode();
        objectNode.put("output",
                objectNodeConverter.toObjectNode(associates,
                        commandInput.getStartTimestamp(),
                        commandInput.getEndTimestamp()));
        return objectNode;
    }
}
