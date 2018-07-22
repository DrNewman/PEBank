package com.pebank.demo.validator;

import com.pebank.demo.Constants;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CustomerInputDataValidator {

    public static void validateNewCustomerParams(String name, String address, String birthday) {
        if (StringUtils.isEmptyOrWhitespace(name)) {
            throw new IllegalArgumentException("Parameter \"name\" have not correct value \"" + name + "\"");
        }
        if (Constants.CASHBOX_VALUE.equals(name)) {
            throw new IllegalArgumentException("Illegal name of customer \"" + name + "\"");
        }
        if (!StringUtils.isEmpty(birthday)) {
            try {
                LocalDate localDate = LocalDate.parse(birthday);
                if (localDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Birthday can't be in the future. birthday: \"" + birthday + "\"");
                }
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Parameter \"birthday\" have not correct value \"" + birthday + "\"");
            }
        }
    }

}
