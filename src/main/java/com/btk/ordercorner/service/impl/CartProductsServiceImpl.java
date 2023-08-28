package com.btk.ordercorner.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.btk.ordercorner.model.entity.CartProducts;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.model.entity.ShoppingCart;
import com.btk.ordercorner.repository.CartProductsRepository;
import com.btk.ordercorner.repository.ProductRepository;
import com.btk.ordercorner.repository.ShoppingCartRepository;
import com.btk.ordercorner.service.CartProductsService;

@Service
public class CartProductsServiceImpl implements CartProductsService {

    private CartProductsRepository cartProductsRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private ProductRepository productRepository;

    public CartProductsServiceImpl(CartProductsRepository cartProductsRepository, 
                        ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository) {
        this.cartProductsRepository = cartProductsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CartProducts> getAllCartProducts() {
        return cartProductsRepository.findAll();
    }

    @Override
    public int addProductInCart(int cartId, int productId, int productQuantity) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId).get();
        Product product = productRepository.findById(productId).get();
        CartProducts cartProducts = new CartProducts(cart, product, productQuantity);
        cartProductsRepository.save(cartProducts);
        return 1;
    }

    @Override
    public int getProductQuantityByCartId(int cartId, int productId){
        CartProducts cartProducts = cartProductsRepository.findCartProductByCartAndProductId(cartId, productId);
        return cartProducts.getProductQuantity();
    }
    
}
