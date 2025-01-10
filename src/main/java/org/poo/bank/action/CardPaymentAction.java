package org.poo.bank.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.account.Associate;
import org.poo.bank.entity.account.Associates;
import org.poo.bank.entity.account.ServicePlan;
import org.poo.bank.entity.user.User;
import org.poo.bank.entity.account.Account;
import org.poo.bank.entity.account.card.Card;
import org.poo.bank.entity.account.card.CardStatus;
import org.poo.bank.entity.account.card.CardType;
import org.poo.bank.entity.transaction.*;
import org.poo.bank.notification.TransactionNotifier;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CardPaymentAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        try {
            Commerciant commerciant = bank.getCommerciant(commandInput.getCommerciant());
            if (commandInput.getEmail() == null) {
                throw new RuntimeException("User not found");
            }

            Card card = bank.getCard(commandInput.getCardNumber());

            if (card == null) {
                throw new RuntimeException("Card not found");
            }

            if (!bank.getUser(card.getOwnerEmail()).equals(bank.getCardUser(card))) {
                throw new RuntimeException("User not found");
            }

            Account account = bank.getAccount(card);
            User user = bank.getUser(commandInput.getEmail());

            Double amountSpent = bank.getAmount(commandInput.getAmount(),
                    commandInput.getCurrency(),
                    account.getCurrency());

            Double noCommissionAmount = amountSpent;

            amountSpent += account.getServicePlan().getCommission(amountSpent, account.getCurrency());
            Double cashbackAmount = noCommissionAmount * commerciant
                    .getCashbackStrategy()
                    .getCashbackRate(account, commerciant, noCommissionAmount);

            Double noCashBackAmount = amountSpent;
            Double finalAmountSpent = amountSpent - cashbackAmount;

            Associates associates = bank.getAssociates(account);
            TransferType transferResult;
            if (associates == null) {
                if (!bank.getUser(card).getEmail().equals(commandInput.getEmail())) {
                    throw new RuntimeException("Card not found");
                }
                transferResult = account.subtractBalance(finalAmountSpent, card);
            } else if (bank.isAssociate(account, user)) {
                if (associates.canPay(user, finalAmountSpent, account.getCurrency())) {
                    transferResult = account.subtractBalance(finalAmountSpent, card);
                    associates.updateAssociatePayment(user, noCommissionAmount, commerciant, commandInput.getTimestamp());
                } else {
                    transferResult = TransferType.TRANSFER_TYPE_INSUFFICIENT_FUNDS;
                }
            } else {
                throw new RuntimeException("Card not found");
            }

            switch (transferResult) {
                case TRANSFER_TYPE_INSUFFICIENT_FUNDS -> TransactionNotifier.notify(
                        new InsufficientFundsTransaction(commandInput.getTimestamp()),
                        user,
                        account);
                case TRANSFER_TYPE_SUCCESSFUL -> {
                    if (noCommissionAmount != 0) {
                        TransactionNotifier.notify(
                                new CardPaymentTransaction(commandInput.getCommerciant(),
                                        noCommissionAmount,
                                        noCashBackAmount,
                                        commandInput.getTimestamp(),
                                        account),
                                user,
                                account);
                    }
                    if (card.getType().equals(CardType.CARD_TYPE_ONE_TIME)) {
                        Card newCard = Card.builder()
                                .cardNumber(Utils.generateCardNumber())
                                .status(CardStatus.CARD_STATUS_ACTIVE)
                                .type(CardType.CARD_TYPE_ONE_TIME)
                                .ownerEmail(user.getEmail())
                                .build();

                        bank.deleteCard(card, account);
                        bank.addUsedCard(card);
                        TransactionNotifier.notify(
                                new DeleteCardTransaction(
                                        account.getIban(),
                                        card.getCardNumber(),
                                        card.getOwnerEmail(),
                                        commandInput.getTimestamp()),
                                user,
                                account);

                        bank.addCard(account, newCard);
                        TransactionNotifier.notify(new CreateCardTransaction(
                                        account.getIban(),
                                        newCard.getCardNumber(),
                                        user.getEmail(),
                                        commandInput.getTimestamp()),
                                user,
                                account);
                    }

                    if (account.canUpgradePlan(amountSpent)) {
                        account.setServicePlan(ServicePlan.GOLD);
                        TransactionNotifier.notify(new UpgradePlanTransaction(
                                        ServicePlan.GOLD.getName(),
                                        account.getIban(),
                                        commandInput.getTimestamp()),
                                user,
                                account);
                    }
                }
                case TRANSFER_TYPE_FROZEN_CARD -> TransactionNotifier.notify(
                        new CardStatusTransaction(
                                TransactionMessage.TRANSACTION_MESSAGE_CARD_STATUS,
                                CardStatus.CARD_STATUS_FROZEN,
                                commandInput.getTimestamp()),
                        user,
                        account);
                default -> {
                    return null;
                }
            }
        } catch (RuntimeException e) {
            return executeError(e.getMessage(), commandInput.getTimestamp());
        }

        return null;
    }
}
