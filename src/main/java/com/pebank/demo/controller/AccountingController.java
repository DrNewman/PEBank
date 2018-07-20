package com.pebank.demo.controller;

import com.pebank.demo.entity.Account;
import com.pebank.demo.entity.Customer;
import com.pebank.demo.repository.AccountRepository;
import com.pebank.demo.repository.CustomerRepository;
import com.pebank.demo.validator.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class AccountingController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public String getWelcomePage() {
        return "welcome";
    }

    @GetMapping("/customers")
    public String addAndGetAllCustomers(@RequestParam(required=false) String action,
                                        @RequestParam(required=false) String name,
                                        @RequestParam(required=false) String address,
                                        @RequestParam(required=false) String birthday,
                                        @RequestParam(required=false) String customerId,
                                        Model model) {
        if (!StringUtils.isEmpty(action)) {
            if ("Add new customer".equals(action)) {
                CustomerValidator.validateNewCustomerParams(name, address, birthday);
                Customer newCustomer = new Customer(name, address, !StringUtils.isEmpty(birthday) ? LocalDate.parse(birthday) : null);
                customerRepository.save(newCustomer);
            } else {
                if ("Remove customer".equals(action)) {
                    Optional<Customer> customerOptional = this.customerRepository.findById(Long.parseLong(customerId));
                    if (customerOptional.isPresent()) {
                        customerRepository.delete(customerOptional.get());
                    } else {
                        throw new IllegalArgumentException("Customer with id = " + customerId + " is not exist");
                    }
                }
            }
        }

        List<Customer> customers = StreamSupport
                .stream(this.customerRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
        model.addAttribute("customers", customers);
        return "customers";
    }


    @GetMapping("/customer/{customerId}")
    public String getCustomerInfo(@PathVariable String customerId, @RequestParam(required=false) String action, Model model) {
        Optional<Customer> customerOptional = this.customerRepository.findById(Long.parseLong(customerId));
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (!StringUtils.isEmpty(action) && "Add new account".equals(action)) {
                Account newAccount = new Account(customer, 0);
                accountRepository.save(newAccount);
            }
            model.addAttribute("customer", customer);
            List<Account> accounts = StreamSupport
                    .stream(this.accountRepository.findAllByCustomer(customer).spliterator(), true)
                    .collect(Collectors.toList());
            model.addAttribute("accounts", accounts);
        } else {
            throw new IllegalArgumentException("Customer with id = " + customerId + " is not exist");
        }

        return "customer";
    }

    @GetMapping("/customer/transactions/{accountId}")
    @ResponseBody
    public String getTransactionsOfAccount(@PathVariable String accountId, Model model) {
        return "Under construction :(";
    }

}
