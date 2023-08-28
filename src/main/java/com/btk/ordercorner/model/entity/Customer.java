package com.btk.ordercorner.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Musteriler")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musteri_id")
    private int customerId;

    @Column(name = "musteri_adi")
    @Size(max = 100)
    private String customerFirstName;

    @Column(name = "musteri_soyadi")
    @Size(max = 100)
    private String customerLastName;

    @Column(name = "kullanici_adi")
    private String username;

    @Column(name = "eposta")
    private String mailAddress;

    @Column(name = "sifre")
    private String password;

    @Column(name = "bakiye")
    private double wallet;

    @Column(name = "kullanici_rolu")
    private String roles;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
