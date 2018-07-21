package com.pebank.demo.repository;

import com.pebank.demo.entity.Account;
import com.pebank.demo.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Iterable<Transaction> findAllBySenderOrReceiver(Account sender, Account receiver);
    Iterable<Transaction> findAllBySenderInOrReceiverIn(Collection<Account> senders, Collection<Account> receivers);
    Iterable<Transaction> findAllBySenderInOrReceiverInAndDateBetween(Collection<Account> senders, Collection<Account> receivers, LocalDateTime fromDate, LocalDateTime toDate);
    Iterable<Transaction> findAllByDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
