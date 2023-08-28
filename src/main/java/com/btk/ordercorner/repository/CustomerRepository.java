package com.btk.ordercorner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btk.ordercorner.model.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    List<Customer> findByUsernameIgnoreCase(String username);

    Optional<Customer> findByUsername(String username);
}
