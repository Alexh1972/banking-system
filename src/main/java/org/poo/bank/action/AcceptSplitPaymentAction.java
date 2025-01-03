package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class AcceptSplitPaymentAction extends Action {
    @Override
    public ObjectNode execute(Bank bank, CommandInput commandInput) {
        return null;
    }
}
