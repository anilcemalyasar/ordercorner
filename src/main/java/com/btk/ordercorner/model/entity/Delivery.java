package com.btk.ordercorner.model.entity;

import com.btk.ordercorner.model.enums.DeliveryStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Teslimat")
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "teslimat_id")
    private int deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "onceki_teslimat_durumu")
    private DeliveryStatus old_status;

    @Enumerated(EnumType.STRING)
    @Column(name = "guncel_teslimat_durumu")
    private DeliveryStatus new_status;

    @OneToOne(mappedBy = "delivery")
    private Order order;


}
