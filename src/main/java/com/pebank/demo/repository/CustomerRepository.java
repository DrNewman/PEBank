package com.pebank.demo.repository;

import com.pebank.demo.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
