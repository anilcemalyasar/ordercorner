package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.dto.ShoppingCartDto;
import com.btk.ordercorner.model.vm.AddProductsInCartVm;

public interface ShoppingCartService {
    List<ShoppingCartDto> getAllCarts();
    ShoppingCartDto getCartByCartId(int cartId);
    ShoppingCartDto getCartByCustomerId(int customerId);
    int createNewShoppingCart(AddProductsInCartVm cartVm);
    String deleteCartById(int cartId);
    List<ProductDto> getAllProductsInCartByCustomerId(int customerId);
    String getAllProductsPriceInCart(int customerId);
}
