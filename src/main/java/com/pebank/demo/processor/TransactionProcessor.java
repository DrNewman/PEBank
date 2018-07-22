package com.pebank.demo.processor;

import com.pebank.demo.entity.Account;
import com.pebank.demo.entity.Transaction;
import com.pebank.demo.repository.AccountRepository;
import com.pebank.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionProcessor{

    public static final String SUCCESS_RESPONSE = "Success!";
    public static final String FAIL_RESPONSE = "Fail: ";

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String provideTransaction(Transaction transaction) {
        String response = SUCCESS_RESPONSE;
        try {
            if (transaction.getAmount() <= 0) {
                throw new RuntimeException("Wrong transaction amount: " + transaction.getAmount());
            }
            this.makeTransaction(transaction);
        } catch (RuntimeException e) {
            response = FAIL_RESPONSE + "\"" + e.getMessage() + "\"";
        }

        return response;
    }

    protected synchronized void makeTransaction(Transaction transaction) {
        Optional<Account> sender = Optional.empty();
        Optional<Account> receiver = Optional.empty();
        if (transaction.getSender() != null) {
            sender = this.accountRepository.findById(transaction.getSender().getId());
            if (!sender.isPresent()) {
                throw new RuntimeException("Account №" + transaction.getSender().getId() + " is not exist");
            }
            sender.get().setBalance(sender.get().getBalance() - transaction.getAmount());
            if (sender.get().getBalance() <= 0) {
                sender.get().setBalance(sender.get().getBalance() + transaction.getAmount());
                throw new RuntimeException("Not enough money on account №" + sender.get().getId());
            }
        }
        if (transaction.getReceiver() != null) {
            receiver = this.accountRepository.findById(transaction.getReceiver().getId());
            if (!receiver.isPresent()) {
                throw new RuntimeException("Account №" + transaction.getReceiver().getId() + " is not exist");
            }
            receiver.get().setBalance(receiver.get().getBalance() + transaction.getAmount());
        }
        if (sender.isPresent()) {
            this.accountRepository.save(sender.get());
        }
        if (receiver.isPresent()) {
            this.accountRepository.save(receiver.get());
        }
        this.transactionRepository.save(transaction);
    }

}
