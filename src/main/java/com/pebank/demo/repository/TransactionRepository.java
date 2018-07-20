package com.pebank.demo.repository;

import com.pebank.demo.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
