package com.btk.ordercorner.model.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Sepetler")
public class ShoppingCart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sepet_id")
    private int cartId;

    
    @Column(name = "sepet_tutari")
    private double totalAmount;
    
    @Column(name = "sepet_urun_miktari")
    private int quantity;
    
    // @ManyToMany
    // @JoinTable(
    //     name = "sepet_urunler",
    //     joinColumns = @JoinColumn(name = "sepet_id", referencedColumnName = "sepet_id"),
    //     inverseJoinColumns = @JoinColumn(name = "urun_id", referencedColumnName = "urun_id")
    // )
    // private List<Product> products;

    @OneToMany(
        mappedBy = "cart",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<CartProducts> products = new ArrayList<>();
    
    @OneToOne
    @JoinColumn(name = "fk_musteri_id", referencedColumnName = "musteri_id")
    private Customer customer;

    @Column(name = "aktif")
    private boolean softDeleted = true;

    public void addProduct(Product product) {
        CartProducts cartProducts = new CartProducts(this, product);
        products.add(cartProducts);
        product.getCarts().add(cartProducts);
    
    } 
    
    public void removeProduct(Product product) {
        for (Iterator<CartProducts> iterator = products.iterator();
             iterator.hasNext(); ) {
            CartProducts cartProducts = iterator.next();
             
            if (cartProducts.getCart().equals(this) &&
                    cartProducts.getProduct().equals(product)) {
                iterator.remove();
                cartProducts.getProduct().getCarts().remove(cartProducts);
                cartProducts.setCart(null);
                cartProducts.setProduct(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCart)) return false;
        ShoppingCart cart = (ShoppingCart) o;
        return cartId == cart.getCartId();
    }

    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
