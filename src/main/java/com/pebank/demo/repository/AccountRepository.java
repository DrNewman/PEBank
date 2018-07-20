package com.pebank.demo.repository;

import com.pebank.demo.entity.Account;
import com.pebank.demo.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Iterable<Account> findAllByCustomer(Customer customer);

}
