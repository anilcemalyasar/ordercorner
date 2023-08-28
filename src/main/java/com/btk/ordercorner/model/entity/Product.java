package com.btk.ordercorner.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Urunler")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "urun_id")
    private int productId;

    @Column(name = "urun_adi")
    private String productName;

    @Column(name = "urun_aciklamasi")
    private String productDescription;

    @Column(name = "urun_fiyati")
    private double productPrice;

    @Column(name = "urun_stok_durumu")
    private int stockAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_kategori_id", referencedColumnName = "kategori_id")
    private Category category;

    // @ManyToMany(mappedBy = "products")
    // private List<ShoppingCart> shoppingCarts;
    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CartProducts> carts = new ArrayList<>();

}
