package com.btk.ordercorner.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.btk.ordercorner.model.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from siparisler where siparis_tarihi < :startDate and fk_musteri_id = :customerId", nativeQuery = true)
    List<Order> findByOrderDateBefore(@Param("startDate") Date startDate,@Param("customerId") int customerId);

    @Query(value = "select * from siparisler where siparis_tarihi > :startDate and fk_musteri_id = :customerId", nativeQuery = true)
    List<Order> findByOrderDateAfter(@Param("startDate") Date startDate,@Param("customerId") int customerId);


}
