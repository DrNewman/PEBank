package com.pebank.demo.validator;

import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CustomerValidator {

    public static void validateNewCustomerParams(String name, String address, String birthday) {
        if (StringUtils.isEmptyOrWhitespace(name)) {
            throw new IllegalArgumentException("Parameter \"name\" is not correct");
        }
        if (!StringUtils.isEmpty(birthday)) {
            try {
                LocalDate.parse(birthday);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Parameter \"birthday\" is not correct");
            }
        }
    }

}
