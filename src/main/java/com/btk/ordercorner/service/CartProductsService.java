package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.entity.CartProducts;

public interface CartProductsService {
    List<CartProducts> getAllCartProducts();
    int addProductInCart(int cartId, int productId, int productQuantity);
    int getProductQuantityByCartId(int cartId, int productId);
}
