package com.pebank.demo.validator;

import com.pebank.demo.Constants;
import org.thymeleaf.util.StringUtils;

public class TransactionInputDataValidator {

    public static void validateNewTransactionParams(String senderCustomerId,
                                                    String senderAccountId,
                                                    String receiverCustomerId,
                                                    String receiverAccountId,
                                                    String amount) {
        if (!StringUtils.isEmpty(amount)) {
            try {
                long amountLong = Long.parseLong(amount);
                if (amountLong <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Parameter \"amount\" have not correct value \"" + amount + "\"");
            }
        } else {
            throw new IllegalArgumentException("Parameter \"amount\" have not correct value \"" + amount + "\"");
        }
        if (StringUtils.isEmpty(senderAccountId) && (StringUtils.isEmpty(senderCustomerId) || !Constants.CASHBOX_VALUE.equals(senderCustomerId))) {
            throw new IllegalArgumentException("Parameter \"senderAccountId\" have not correct value \"" + senderAccountId + "\"");
        }
        if (StringUtils.isEmpty(receiverAccountId) && (StringUtils.isEmpty(receiverCustomerId) || !Constants.CASHBOX_VALUE.equals(receiverCustomerId))) {
            throw new IllegalArgumentException("Parameter \"receiverAccountId\" have not correct value \"" + receiverAccountId + "\"");
        }
    }
}
