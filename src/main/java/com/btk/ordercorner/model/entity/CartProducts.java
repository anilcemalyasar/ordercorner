package com.btk.ordercorner.model.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sepet_urunler")
public class CartProducts {
    @EmbeddedId
    private CartProductId cartProductId;
 
    @ManyToOne(fetch = FetchType.EAGER)
    // @MapsId("sepet_id")
    private ShoppingCart cart;
 
    @ManyToOne(fetch = FetchType.EAGER)
    // @MapsId("urun_id")
    private Product product;

    @Column(name = "urun_miktari")
    private int productQuantity;

    private CartProducts() {}
 
    public CartProducts(ShoppingCart cart, Product product) {
        this.cart = cart;
        this.product = product;
        this.cartProductId = new CartProductId(cart.getCartId(), product.getProductId());
    }

    public CartProducts(ShoppingCart cart, Product product, int productQuantity) {
        this.cart = cart;
        this.product = product;
        this.productQuantity = productQuantity;
        this.cartProductId = new CartProductId(cart.getCartId(), product.getProductId());
    }
 
    //Getters and setters omitted for brevity
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        CartProducts that = (CartProducts) o;
        return Objects.equals(cart, that.cart) &&
            Objects.equals(product, that.product);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(cart, product);
    }
}

