package com.btk.ordercorner.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Siparisler")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siparis_id")
    private int orderId;

    @Column(name = "siparis_tarihi")
    private Date orderDate;

    @Column(name = "siparis_durumu")
    private boolean orderStatus;

    @Column(name = "siparis_tutari")
    private double totalAmount;

    @OneToOne
    @JoinColumn(name = "fk_odeme_turu_id", referencedColumnName = "odeme_turu_id")
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "fk_musteri_id", referencedColumnName = "musteri_id")
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_sepet_id", referencedColumnName = "sepet_id")
    private ShoppingCart cart;

}
