package com.btk.ordercorner.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.exception.product.ProductAlreadyExistsException;
import com.btk.ordercorner.exception.product.ProductNotFoundException;
import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.model.vm.AddProductVm;
import com.btk.ordercorner.model.vm.UpdateProductStockVm;
import com.btk.ordercorner.repository.CategoryRepository;
import com.btk.ordercorner.repository.ProductRepository;
import com.btk.ordercorner.service.ProductService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ModelMapperManager modelMapperManager;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapperManager modelMapperManager) {
        this.productRepository = productRepository;
        this.modelMapperManager = modelMapperManager;
    }


    // burada Cacheable koyacağız 
    @Cacheable(value = "products")
    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> list = productRepository.findAll();
        return list.stream().map(product -> modelMapperManager.forResponse().map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#productId")
    @Override
    public ProductDto getProductById(int productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            String errorMessage = productId + " ID numarasına sahip ürün bulunmamaktadır!";
            logger.error(errorMessage);
            throw new ProductNotFoundException(errorMessage);
        }
        Product product = optionalProduct.get();
        return modelMapperManager.forResponse().map(product, ProductDto.class);
    }

    @CacheEvict(value = "products", key = "#productId")
    @Override
    public String deleteProductById(int productId) {
        Authentication auth = getAuth();
        if(!existsById(productId)) {
            String errorMessage = productId + " ID numarasına sahip ürün bulunmamaktadır!";
            logger.error(errorMessage);
            throw new ProductNotFoundException(errorMessage);
        }
        productRepository.deleteById(productId);
        String message = auth.getName() + " isimli admin " + productId + " ID numaralı ürün kaydını sildi!";
        logger.info(message);
        return message;
    }

    @Override
    public boolean existsById(int productId) {
        return productRepository.existsById(productId) ? true : false;
    }

    @CachePut(value = "products", key = "#productVm.productId")
    @Override
    public int addNewProduct(AddProductVm productVm) {
        Authentication auth = getAuth();
        Product product = productRepository.findByProductNameIgnoreCase(productVm.getProductName());
        if (product != null) {
            String errorMessage = product.getProductName() + " isimde bir ürün zaten kayıtlıdır!";
            logger.error(errorMessage);
            throw new ProductAlreadyExistsException(errorMessage);
        }
        ProductDto productDto = modelMapperManager.forResponse().map(productVm, ProductDto.class);
        Product realProduct = modelMapperManager.forRequest().map(productDto, Product.class);
        productRepository.save(realProduct);
        String message = auth.getName() + " isimli admin " + realProduct.getProductId() + " ID numaralı " + realProduct.getProductName() + " isimli ürünü ekledi!";
        logger.info(message);
        return realProduct.getProductId();
    }

    @Override
    public ProductDto searchProductByName(String productName){
        return modelMapperManager.forResponse().map(productRepository.findByProductNameIgnoreCase(productName), ProductDto.class);
    }

    @Override
    public List<ProductDto> getProductsByCategoryName(String categoryName) {
        List<Product> products = productRepository.findAll()
            .stream().filter(product -> product.getCategory().getCategoryName().toLowerCase().equals(categoryName.toLowerCase())).toList();
        return products.stream().map(product -> modelMapperManager.forResponse().map(product, ProductDto.class))
                .collect(Collectors.toList()); 
    }

    public Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }


    @Override
    public List<ProductDto> getProductsWhosePriceBigger(double productPrice) {
        List<Product> products = productRepository.findAll().stream().filter(product -> product.getProductPrice() > productPrice).toList();
        return products.stream()
            .map(product -> modelMapperManager.forResponse().map(product, ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsWhosePriceLower(double productPrice) {
        List<Product> products = productRepository.findAll().stream().filter(product -> product.getProductPrice() < productPrice).toList();
        return products.stream()
            .map(product -> modelMapperManager.forResponse().map(product, ProductDto.class)).collect(Collectors.toList());
    }


    @CachePut(value = "products", key = "#productStockVm.productId")
    @Override
    public String updateProductStock(UpdateProductStockVm productStockVm) {
        Authentication auth = getAuth();
        Product product = productRepository.findById(productStockVm.getProductId()).get();
        if(productStockVm.getStockAmount() > 10) {
            logger.error("Bir ürünün stoğunu bir seferde en fazla 10 arttırabilirsiniz!");
            throw new AccessDeniedException("Bir ürünün stoğunu bir seferde en fazla 10 arttırabilirsiniz!");
        }
        product.setStockAmount(product.getStockAmount() + productStockVm.getStockAmount());
        String message = auth.getName() 
        + " isimli admin " + product.getProductName() + " ürününün stoğunu " + product.getStockAmount() + " sayısına güncelledi!";
        productRepository.save(product);
        logger.info(message);
        return message;
    }

}
