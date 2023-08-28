package com.btk.ordercorner.model.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CartProductId {
    
    @Column(name = "sepet_id")
    private int cartId;

    @Column(name = "urun_id")
    private int productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        CartProductId that = (CartProductId) o;
        return Objects.equals(cartId, that.cartId) &&
                Objects.equals(productId, that.productId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId);
    }

}
