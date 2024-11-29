package org.poo.bank.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.entity.account.card.CardType;
import org.poo.fileio.CommandInput;

public abstract class Action {
    private static final ObjectMapper mapper = new ObjectMapper();
    public abstract ObjectNode execute(Bank bank, CommandInput commandInput);

    public static Action toAction(String action) {
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
//                case "spendingsReport" -> new GetReportAction();
                case "changeInterestRate" -> new ChangeInterestRate();
                case "addInterest" -> new AddInterestAction();
                default -> throw new IllegalArgumentException("Unexpected action value: " + action);
            };
    }

    public ObjectNode executeError(String message, int timestamp) {
        ObjectNode objectNode = mapper.createObjectNode();
        ObjectNode detailNode = mapper.createObjectNode();

        detailNode.put("description", message);
        detailNode.put("timestamp", timestamp);

        objectNode.put("output", detailNode);
        return objectNode;
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }
}
