package com.pebank.demo.controller;

import com.pebank.demo.Constants;
import com.pebank.demo.entity.Account;
import com.pebank.demo.entity.Customer;
import com.pebank.demo.entity.Transaction;
import com.pebank.demo.processor.TransactionProcessor;
import com.pebank.demo.repository.AccountRepository;
import com.pebank.demo.repository.CustomerRepository;
import com.pebank.demo.repository.TransactionRepository;
import com.pebank.demo.validator.CustomerInputDataValidator;
import com.pebank.demo.validator.TransactionInputDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class AccountingController {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionProcessor transactionProcessor;

    @RequestMapping("/")
    public String getWelcomePage() {
        return "welcome";
    }

    @RequestMapping("/customers")
    public String addAndGetAllCustomers(@RequestParam(required=false) String action,
                                        @RequestParam(required=false) String name,
                                        @RequestParam(required=false) String address,
                                        @RequestParam(required=false) String birthday,
                                        @RequestParam(required=false) String customerId,
                                        Model model) {
        if (!StringUtils.isEmpty(action)) {
            if (Constants.NEW_CUSTOMER_ACTION.equals(action)) {
                CustomerInputDataValidator.validateNewCustomerParams(name, address, birthday);
                Customer newCustomer = new Customer(name, address, !StringUtils.isEmpty(birthday) ? LocalDate.parse(birthday) : null);
                customerRepository.save(newCustomer);
            }
        }

        List<Customer> customers = StreamSupport
                .stream(this.customerRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
        model.addAttribute("customers", customers);
        return "customers";
    }


    @RequestMapping("/customer/{customerId}")
    public String getCustomerInfo(@PathVariable String customerId,
                                  @RequestParam(required=false) String action,
                                  Model model) {
        Optional<Customer> customerOptional = this.customerRepository.findById(Long.parseLong(customerId));
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (!StringUtils.isEmpty(action) && Constants.NEW_ACCOUNT_ACTION.equals(action)) {
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
        model.addAttribute("currency", Constants.CURRENCY);

        return "customer";
    }

    @RequestMapping("/transactions")
    public String getTransactions(@RequestParam(required=false) String fromDate,
                                  @RequestParam(required=false) String toDate,
                                  @RequestParam(required=false) String customerId,
                                  @RequestParam(required=false) String accountId,
                                  Model model) {

        Optional<Customer> customer = Optional.empty();
        Optional<Account> account = Optional.empty();

        model.addAttribute("toDate", toDate);

        if (!StringUtils.isEmpty(customerId)) {
            try {
                Long.parseLong(customerId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parameter \"customerId\" have not correct value \"" + customerId + "\"");
            }
            customer = this.customerRepository.findById(Long.parseLong(customerId));
            if (customer.isPresent()) {
                model.addAttribute("customerId", customerId);
                List<Account> accounts = StreamSupport
                        .stream(this.accountRepository.findAllByCustomer(customer.get()).spliterator(), true)
                        .collect(Collectors.toList());
                model.addAttribute("accounts", accounts);
            }
        }

        if (!StringUtils.isEmpty(accountId)) {
            try {
                Long.parseLong(accountId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parameter \"accountId\" have not correct value \"" + accountId + "\"");
            }
            account = this.accountRepository.findById(Long.parseLong(accountId));
            if (account.isPresent()) {
                model.addAttribute("accountId", accountId);
            }
        }

        LocalDateTime fromDateTime = LocalDateTime.MIN;
        if (!StringUtils.isEmpty(fromDate)) {
            try {
                LocalDate.parse(fromDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Parameter \"fromDate\" have not correct value \"" + fromDate + "\"");
            }
            fromDateTime = LocalDate.parse(fromDate, DateTimeFormatter.ISO_LOCAL_DATE).atTime(LocalTime.MIN);
            model.addAttribute("fromDate", fromDate);
        }
        LocalDateTime toDateTime = LocalDateTime.MAX;
        if (!StringUtils.isEmpty(toDate)) {
            try {
                LocalDate.parse(toDate, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Parameter \"toDate\" have not correct value \"" + toDate + "\"");
            }
            toDateTime = LocalDate.parse(toDate, DateTimeFormatter.ISO_LOCAL_DATE).atTime(LocalTime.MAX);
            model.addAttribute("toDate", toDate);
        }

        Collection<Account> accounts = new ArrayList<>();
        if (account.isPresent()) {
            accounts.add(account.get());
        } else {
            if (customer.isPresent()) {
                accounts.addAll(StreamSupport
                        .stream(this.accountRepository.findAllByCustomer(customer.get()).spliterator(), true)
                        .collect(Collectors.toList()));
            }
        }
        List<Transaction> transactions;
        if (fromDate != null || toDate != null) {
            if (!accounts.isEmpty()) {
                transactions = StreamSupport
                        .stream(this.transactionRepository.findAllBySenderInOrReceiverInAndDateBetween(accounts, accounts, fromDateTime, toDateTime).spliterator(), true)
                        .collect(Collectors.toList());
            } else {
                transactions = StreamSupport
                        .stream(this.transactionRepository.findAllByDateBetween(fromDateTime, toDateTime).spliterator(), true)
                        .collect(Collectors.toList());
            }
        } else {
            if (!accounts.isEmpty()) {
                transactions = StreamSupport
                        .stream(this.transactionRepository.findAllBySenderInOrReceiverInAndDateBetween(accounts, accounts, fromDateTime, toDateTime).spliterator(), true)
                        .collect(Collectors.toList());
            } else {
                transactions = StreamSupport
                        .stream(this.transactionRepository.findAll().spliterator(), true)
                        .collect(Collectors.toList());
            }
        }
        model.addAttribute("transactions", transactions);

        List<Customer> customers = StreamSupport
                .stream(this.customerRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
        model.addAttribute("customers", customers);

        model.addAttribute("currency", Constants.CURRENCY);
        return "transactions";
    }



    @RequestMapping("/create_transaction")
    public String createTransaction(@RequestParam(required=false) String senderCustomerId,
                                    @RequestParam(required=false) String senderAccountId,
                                    @RequestParam(required=false) String receiverCustomerId,
                                    @RequestParam(required=false) String receiverAccountId,
                                    @RequestParam(required=false) String amount,
                                    @RequestParam(required=false) String action,
                                    Model model) {

        Optional<Customer> senderCustomer = Optional.empty();
        Optional<Account> senderAccount = Optional.empty();
        Optional<Customer> receiverCustomer = Optional.empty();
        Optional<Account> receiverAccount = Optional.empty();

        if (!StringUtils.isEmpty(senderCustomerId) && !Constants.CASHBOX_VALUE.equals(senderCustomerId)) {
            try {
                Long.parseLong(senderCustomerId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parameter \"senderCustomerId\" have not correct value \"" + senderCustomerId + "\"");
            }
            senderCustomer = this.customerRepository.findById(Long.parseLong(senderCustomerId));
            if (senderCustomer.isPresent()) {
                model.addAttribute("senderCustomerId", senderCustomerId);
                List<Account> accounts = StreamSupport
                        .stream(this.accountRepository.findAllByCustomer(senderCustomer.get()).spliterator(), true)
                        .collect(Collectors.toList());
                model.addAttribute("senderAccounts", accounts);
            }
        }

        if (!StringUtils.isEmpty(senderAccountId)) {
            try {
                Long.parseLong(senderAccountId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parameter \"senderAccountId\" have not correct value \"" + senderAccountId + "\"");
            }
            senderAccount = this.accountRepository.findById(Long.parseLong(senderAccountId));
            if (senderAccount.isPresent()) {
                model.addAttribute("senderAccountId", senderAccountId);
            }
        }

        if (!StringUtils.isEmpty(receiverCustomerId) && !Constants.CASHBOX_VALUE.equals(receiverCustomerId)) {
            try {
                Long.parseLong(receiverCustomerId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parameter \"receiverCustomerId\" have not correct value \"" + receiverCustomerId + "\"");
            }
            receiverCustomer = this.customerRepository.findById(Long.parseLong(receiverCustomerId));
            if (receiverCustomer.isPresent()) {
                model.addAttribute("receiverCustomerId", receiverCustomerId);
                List<Account> accounts = StreamSupport
                        .stream(this.accountRepository.findAllByCustomer(receiverCustomer.get()).spliterator(), true)
                        .collect(Collectors.toList());
                model.addAttribute("receiverAccounts", accounts);
            }
        }

        if (!StringUtils.isEmpty(receiverAccountId)) {
            try {
                Long.parseLong(receiverAccountId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Parameter \"receiverAccountId\" have not correct value \"" + receiverAccountId + "\"");
            }
            receiverAccount = this.accountRepository.findById(Long.parseLong(receiverAccountId));
            if (receiverAccount.isPresent()) {
                model.addAttribute("receiverAccountId", receiverAccountId);
            }
        }

        model.addAttribute("amount", amount);

        if (Constants.NEW_TRANSACTION_ACTION.equals(action)) {
            TransactionInputDataValidator.validateNewTransactionParams(senderCustomerId, senderAccountId, receiverCustomerId, receiverAccountId, amount);
            Transaction newTransaction = new Transaction(senderAccount.isPresent() ? senderAccount.get() : null,
                    receiverAccount.isPresent() ? receiverAccount.get() : null,
                    Long.parseLong(amount), LocalDateTime.now());
            String response = transactionProcessor.provideTransaction(newTransaction);
            model.addAttribute("response", response);
        }

        List<Customer> customers = StreamSupport
                .stream(this.customerRepository.findAll().spliterator(), true)
                .collect(Collectors.toList());
        model.addAttribute("customers", customers);

        model.addAttribute("currency", Constants.CURRENCY);
        return "create_transaction";
    }

}
