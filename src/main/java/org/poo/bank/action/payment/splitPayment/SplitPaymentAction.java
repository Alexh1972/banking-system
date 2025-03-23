package org.poo.bank.action.payment.splitPayment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.action.Action;
import org.poo.bank.entity.SplitPayment;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentAction extends Action {
    @Override
    public final ObjectNode execute(final Bank bank, final CommandInput commandInput) {
        SplitPaymentType paymentType = SplitPaymentType
                .getSplitPaymentType(
                        commandInput.getSplitPaymentType()
                );
        List<Double> amounts = commandInput.getAmountForUsers();


        if (paymentType.equals(SplitPaymentType.EQUAL)) {
            amounts = new ArrayList<>();
            int numberPayers = commandInput.getAccounts().size();
            for (int i = 0; i < numberPayers; i++) {
                amounts.add(commandInput.getAmount() / numberPayers);
            }
        }

        SplitPayment splitPayment = new SplitPayment(
                commandInput.getAccounts(),
                amounts,
                commandInput.getCurrency(),
                paymentType.getValue(),
                commandInput.getTimestamp()
        );
        bank.addSplitPayment(splitPayment);
        return null;
    }

    @Getter
    private enum SplitPaymentType {
        CUSTOM("custom"), EQUAL("equal");
        private String value;
        SplitPaymentType(final String value) {
           this.value = value;
        }

        public static SplitPaymentType getSplitPaymentType(final String value) {
            for (SplitPaymentType type : SplitPaymentType.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }

            return EQUAL;
        }
    }
}
