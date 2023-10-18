package com.hiepnh.springdocker.repository;

import com.hiepnh.springdocker.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByPhone(String phone);
    Page<Customer> findAllByNameContains(String name, Pageable pageable);
}
