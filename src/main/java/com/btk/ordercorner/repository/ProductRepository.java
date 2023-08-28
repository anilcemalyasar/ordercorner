package com.btk.ordercorner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btk.ordercorner.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByProductNameIgnoreCase(String productName);

} 