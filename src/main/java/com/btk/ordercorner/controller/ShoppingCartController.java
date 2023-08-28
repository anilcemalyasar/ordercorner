package com.btk.ordercorner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.dto.ShoppingCartDto;
import com.btk.ordercorner.model.entity.ShoppingCart;
import com.btk.ordercorner.model.vm.AddProductsInCartVm;
import com.btk.ordercorner.service.ShoppingCartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/carts/")
@Tag(name = "Sepet İşlemleri")
@SecurityRequirement(name = "deneme")
public class ShoppingCartController {
    
    private ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping(value="")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(
        description = "Bu metod, bir müşterinin veya kullanıcıların alışveriş sepetinde bulunan tüm ürünleri görüntüleme amacıyla kullanılır. Müşteriler ve kullanıcılar, bu metod aracılığıyla alışveriş sepetlerindeki ürünlerin listesini elde edebilirler. Böylece alışveriş sepetlerinde hangi ürünlerin bulunduğunu ve bu ürünlerin detaylarını görüntüleyebilirler.",
        summary =   "Bu metod, sepetteki tüm ürünleri listeler.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    public List<ShoppingCartDto> getAllCarts() {
        return shoppingCartService.getAllCarts();
    }

    @GetMapping(value="/{cartId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(
        description = "Bu metod, bir müşterinin veya kullanıcıların belirtilen {id} parametresine göre alışveriş sepeti bilgilerini görüntüleme amacıyla kullanılır. Müşteriler ve kullanıcılar, bu metod aracılığıyla alışveriş sepet bilgilerini elde edebilir. Böylece alışveriş sepetlerinde hangi ürünlerin bulunduğunu ve bu ürünlerin detaylarını görüntüleyebilirler.",
        summary =   "Bu metod, sepet {id} parametresine göre sepet bilgilerini sağlar.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    public ShoppingCartDto getCartByCartId(@PathVariable("cartId") int cartId) {
        return shoppingCartService.getCartByCartId(cartId);
    }

    @Operation(
        description = "Bu metod, belirtilen ({id} müşterinin id parametresine göre) müşterinin alışveriş sepetindeki tüm ürünleri listeler.",
        summary =   "Bu metod, mevcut müşterinin alışveriş sepetindeki ürünleri listeler.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    @GetMapping(value="/customers/{customerId}/cart")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ShoppingCartDto getCartByCustomerId(@PathVariable("customerId") int customerId) {
        return shoppingCartService.getCartByCustomerId(customerId);
    }
    
    @Operation(
        description = "Bu metod, siparişi tanımlanan mevcut kullanıcının yeni bir alışveriş sepeti oluşturmasını sağlar.",
        summary =   "Bu metod, mevcut müşterinin yeni sepet oluşturmasını sağlar.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    @PostMapping(value="/customers/products")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public int createNewShoppingCart(@Valid @RequestBody AddProductsInCartVm shoppingCartVm) {
        return shoppingCartService.createNewShoppingCart(shoppingCartVm);
    }    


    @Operation(
        description = "Bu metod, belirtilen ({id} müşterinin id parametresine göre) müşterinin alışveriş sepetindeki tüm ürünleri listeler sağlar.",
        summary =   "Bu metod, belirtilen müşterinin alışveriş sepetindeki tüm ürünleri listeler.",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    @GetMapping(value="/customers/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<ProductDto> getAllProductsInCartByCustomerId(@PathVariable("customerId") int customerId) {
        return shoppingCartService.getAllProductsInCartByCustomerId(customerId);
    }
    
    
    @Operation(
        description = "Bu metod, belirtilen ({id} müşterinin id parametresine göre) müşterinin alışveriş sepetinin toplam tutarını döndürür.",
        summary =   "Bu metod, belirtilen müşterinin alışveriş sepetinin toplam tutarını döndürür",
        responses = {
            @ApiResponse(
                description = "Başarılı",
                responseCode = "200"
            ),
            @ApiResponse(
                description = "Geçersiz istek",
                responseCode = "400"
            ),
            @ApiResponse(
                description = "Yetkisiz işlem",
                responseCode = "403"
            ),
            @ApiResponse(
                description = "Bulunamadı",
                responseCode = "404"
            )
        }
    )
    @GetMapping(value="/customers/{customerId}/cartTotal")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getAllProductsPriceInCart(@PathVariable("customerId") int customerId) {
        return shoppingCartService.getAllProductsPriceInCart(customerId);
    }
    
    

}
