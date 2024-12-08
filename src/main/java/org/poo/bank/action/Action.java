package org.poo.bank.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.card.CardType;
import org.poo.fileio.CommandInput;

public abstract class Action {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Executes a bank action.
     * @param bank The bank.
     * @param commandInput The input of the action.
     * @return The result of the action.
     */
    public abstract ObjectNode execute(Bank bank, CommandInput commandInput);

    /**
     * Factory for the bank action.
     * @param action The action's name.
     * @return The instance of the action.
     */
    public static Action toAction(final String action) {
        return switch (action) {
                case "printUsers" ->  new GetUsersAction();
                case "addAccount" -> new AddAccountAction();
                case "addFunds" -> new AddFundAction();
                case "createCard" -> new CreateCardAction(CardType.CARD_TYPE_GENERAL);
                case "createOneTimeCard" -> new CreateCardAction(CardType.CARD_TYPE_ONE_TIME);
                case "deleteAccount" -> new DeleteAccountAction();
                case "deleteCard" -> new DeleteCardAction();
                case "checkCardStatus" -> new CheckCardStatusAction();
                case "payOnline" -> new CardPaymentAction();
                case "setMinimumBalance" -> new SetMinimumBalanceAction();
                case "printTransactions" -> new GetTransactionsAction();
                case "sendMoney" -> new SendMoneyAction();
                case "setAlias" -> new SetAliasAction();
                case "splitPayment" -> new SplitPaymentAction();
                case "report" -> new GetReportAction();
                case "spendingsReport" -> new GetSpendingsReportAction();
                case "changeInterestRate" -> new ChangeInterestRate();
                case "addInterest" -> new AddInterestAction();
                default -> throw new IllegalArgumentException("Unexpected action value: " + action);
            };
    }

    /**
     * Returns the error from an action.
     * @param message The error message.
     * @param timestamp The timestamp of the action.
     * @return The error.
     */
    public final ObjectNode executeError(final String message, final int timestamp) {
        ObjectNode objectNode = MAPPER.createObjectNode();
        ObjectNode detailNode = MAPPER.createObjectNode();

        detailNode.put("description", message);
        detailNode.put("timestamp", timestamp);

        objectNode.put("output", detailNode);
        return objectNode;
    }

    protected final ObjectMapper getMapper() {
        return MAPPER;
    }
}
