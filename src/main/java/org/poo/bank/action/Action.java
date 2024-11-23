package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public abstract class Action {
    public abstract ObjectNode execute(Bank bank, CommandInput commandInput);

    public static Action toAction(String action) {
        return switch (action) {
                case "printUsers" ->  new PrintUsersAction();
                case "addAccount" -> new AddAccountAction();
                case "addFunds" -> new AddFundAction();
                case "createCard" -> new CreateCardAction();
                default -> throw new IllegalStateException("Unexpected action value: " + action);
            };
    }
}
