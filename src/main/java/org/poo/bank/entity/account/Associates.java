package org.poo.bank.entity.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.bank.Bank;
import org.poo.bank.BankSingleton;
import org.poo.bank.entity.Commerciant;
import org.poo.bank.entity.user.User;
import org.poo.bank.visitor.ObjectNodeAcceptor;
import org.poo.bank.visitor.ObjectNodeVisitor;

import java.util.*;

@Data
public class Associates {
    private List<Associate> associates;
    private Map<User, Associate> associateMap;
    private Map<Commerciant, List<Associate.AssociateInformation>> informationMap;
    private Double paymentLimit;
    private Double depositLimit;
    private String currency;
    private String iban;

    public Associates(Account account) {
        Bank bank = BankSingleton.getInstance();
        associates = new ArrayList<>();
        associateMap = new HashMap<>();
        informationMap = new HashMap<>();
        paymentLimit = bank.getAmount(500.0, getDefaultCurrency(), account.getCurrency());
        depositLimit = bank.getAmount(500.0, getDefaultCurrency(), account.getCurrency());
        iban = account.getIban();
        currency = "RON";
    }

    public void addAssociate(User user, Associate.AssociateType type) {
        Associate associate = new Associate(user, type);
        associates.add(associate);

        if (associateMap.containsKey(user)) {
            Associate oldAssociate = associateMap.get(user);

            if (Associate.AssociateType.isUpgrade(type, oldAssociate.getType())) {
                associateMap.put(user, associate);
            }
//            oldAssociate.setType(type);
        } else {
            associateMap.put(user, associate);
        }
    }

    public void addAssociate(User user, String type) {
        addAssociate(user, Associate.AssociateType.getAssociateType(type));
    }

    public boolean changeDepositLimit(User user, Double amount) {
        if (getAssociate(user).getType().getCanChangeLimits()) {
            depositLimit = amount;
            return true;
        }

        return false;
    }

    public boolean changePaymentLimit(User user, Double amount) {
        Associate associate = getAssociate(user);
        if (associate == null) {
            return true;
        }

        if (associate.getType().getCanChangeLimits()) {
            paymentLimit = amount;
            return true;
        }

        return false;
    }

    public Associate getAssociate(User user) {
        return associateMap.get(user);
    }

    public boolean canPay(User user, Double amount, String currency) {
        Associate associate = getAssociate(user);
        Associate.AssociateType associateType = associate.getType();

        return !associateType.getHasPaymentLimit() ||  amount <= paymentLimit;
    }

    public void updateAssociatePayment(User user, Double amount, Integer timestamp) {
        Associate associate = getAssociate(user);
        associate.addPayment(user, amount, timestamp);
    }
    public void updateAssociatePayment(User user, Double amount, Commerciant commerciant, Integer timestamp) {
        Associate associate = getAssociate(user);
        Associate.AssociateInformation associateInformation = associate.addPayment(user, amount, timestamp);

        List<Associate.AssociateInformation> list = informationMap.get(commerciant);
        if (list == null) {
            list = new ArrayList<>();
            informationMap.put(commerciant, list);
        }

        list.add(associateInformation);
    }

    public List<Associate.AssociateInformation> getCommerciantSpending(Commerciant commerciant, Integer start, Integer end) {
        List<Associate.AssociateInformation> list = informationMap.get(commerciant);

        if (list == null) {
            return null;
        }

        return list.stream().filter(a -> a.getTimestamp() >= start && a.getTimestamp() <= end).toList();
    }

    public List<Commerciant> getCommerciantList() {
        return informationMap.keySet().stream().sorted(Comparator.comparing(Commerciant::getName)).toList();
    }
    public void updateAssociateDeposit(User user, Double amount, Integer timestamp) {
        Associate associate = getAssociate(user);
        associate.addDeposit(amount, timestamp);
    }

    public boolean canAddFunds(User user, Double amount) {
        Associate.AssociateType associateType = getAssociate(user).getType();

        return associateType.getCanAddFunds() || amount <= depositLimit;
    }

    public String getDefaultCurrency() {
        return "RON";
    }
}
