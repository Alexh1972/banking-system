package org.poo.bank.action.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.AccountType;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.user.User;
import org.poo.fileio.CommandInput;

public class AddFundAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Account account = bank.getAccount(commandInput.getAccount());

            if (account == null) {
                throw new RuntimeException("User not found");
            }

            User user = bank.getUser(commandInput.getEmail());

            if (bank.isAssociate(account, user)) {
                Associates associates = bank.getAssociates(account);
                if (!associates.canAddFunds(user, commandInput.getAmount())) {
                    return null;
                }

                associates.updateAssociateDeposit(user,
                        commandInput.getAmount(),
                        commandInput.getTimestamp());
            } else if (account.getAccountType()
                    .equals(AccountType.ACCOUNT_TYPE_BUSINESS)) {
                return null;
            }
            account.addBalance(commandInput.getAmount());

            if (account.getBalance() >= account.getMinimumBalance()) {
                for (Card card : account.getCards()) {
                    card.setStatus(CardStatus.CARD_STATUS_ACTIVE);
                }
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
