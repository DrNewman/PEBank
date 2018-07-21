package com.pebank.demo.validator;

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
    }
}
