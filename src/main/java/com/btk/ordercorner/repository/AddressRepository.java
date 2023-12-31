package com.btk.ordercorner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btk.ordercorner.model.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    
}
