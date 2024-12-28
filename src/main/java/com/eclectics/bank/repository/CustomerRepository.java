package com.eclectics.bank.repository;

import com.eclectics.bank.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{


    Optional<Customer> findByEmailOrPhone(String email, String phone);
}
