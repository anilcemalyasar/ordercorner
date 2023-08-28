package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.vm.AddProductVm;

public interface ProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProductById(int productId);

    String deleteProductById(int productId);

    boolean existsById(int productId);

    int addNewProduct(AddProductVm productVm);

    ProductDto searchProductByName(String productName);

    List<ProductDto> getProductsByCategoryName(String categoryName);

    List<ProductDto> getProductsWhosePriceBigger(double productPrice);

    List<ProductDto> getProductsWhosePriceLower(double productPrice);

}
