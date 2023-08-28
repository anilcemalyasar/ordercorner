package com.btk.ordercorner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.btk.ordercorner.model.entity.CartProducts;

public interface CartProductsRepository extends JpaRepository<CartProducts, Integer> {
    
    @Query(value = "select * from sepet_urunler su where su.cart_sepet_id = ?1 and su.product_urun_id = ?2", nativeQuery=true)
    CartProducts findCartProductByCartAndProductId(int cartId, int productId);

    @Query(value = "select su.* from sepet_urunler su, sepetler s where s.fk_musteri_id = :customerId and su.cart_sepet_id = s.sepet_id and s.aktif = true", nativeQuery = true)
    List<CartProducts> getCartProductsByCustomerId(@Param("customerId") int customerId);

    @Modifying
    @Query(value = "update sepet_urunler set urun_miktari = :productQuantity where cart_sepet_id = :cartId and product_urun_id = :productId", nativeQuery = true)
    void updateProductQuantityInCart(@Param("cartId") int cartId, @Param("productId") int productId, @Param("productQuantity") int productQuantity);
}
