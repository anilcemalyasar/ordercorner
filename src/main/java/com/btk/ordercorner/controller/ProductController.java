package com.btk.ordercorner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.vm.AddProductVm;
import com.btk.ordercorner.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/products/")
@SecurityRequirement(name = "deneme")
@Tag(name = "Ürün İşlemleri")
public class ProductController {
    
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
        description = "Bu metod, tüm müşteri ve kullanıcıların, sistemde bulunan tüm ürünleri görüntülemesi amacıyla kullanılır. Kullanıcılar, bu metod aracılığıyla platformun tüm ürün verilerine erişebilir ve ürünleri inceleyebilirler. ",
        summary =   "Bu metod, tüm ürünleri listeler.",
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
    @GetMapping(value="all")
    public List<ProductDto> getAllProducts() {
    
        return productService.getAllProducts();
    }

    @Operation(
        description = "Bu metod, belirli bir ürünlere ait verileri listelemek için kullanılır. {id} parametresi, istenen ürünün benzersiz tanımlayıcısını belirtir. API'ye gönderilen bu {id} parametresi sayesinde, ilgili ürüne ait olan Ürünler filtrelenir ve listelenir. ",
        summary =   "Bu metod kullanıcılara,  girilen {id} parametresine sahip ürünü sağlar.",
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
    @GetMapping(value="/{productId}")
    public ProductDto getProductById(@PathVariable("productId") int productId) {
        return productService.getProductById(productId);
    }

    @Operation(
        description = "Bu metod, yalnızca \"Admin\" yetkisine sahip olan kullanıcıların belirtilen \"id\" ürününe göre silme işlemi gerçekleştirmesi için kullanılır. ",
        summary =   "Bu metod, {id} parametresi gönderilen ürünü silmek için kullanılır.",
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
    @DeleteMapping(value="/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteProductById(@PathVariable("productId") int productId) {
        return productService.deleteProductById(productId);
    }

    
    @Operation(
        description = "Bu metod, yalnızca \"Admin\" rolüne sahip kullanıcıların yeni ürünler eklemesi için kullanılır. Yeni bir ürün eklemek isteyen \"Admin\", bu metod aracılığıyla gerekli bilgileri sisteme iletebilir.",
        summary =   "Bu metod, sadece \"Admin\" rolündeki kullanıcıların yeni ürünler eklemesine izin verir.",
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
    @PostMapping(value="")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public int addNewProduct(@Valid @RequestBody AddProductVm productVm) {
        return productService.addNewProduct(productVm);
    }
    

    @Operation(
        description = "Bu metod,ürünleri kategori adına göre filtreleyerek listeler",
        summary =   "Bu metod, ürünleri kategori adına göre filtreleyerek listeler.",
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
    @GetMapping(value="/search/{categoryName}")
    public List<ProductDto> getProductsByCategoryName(@PathVariable("categoryName") String categoryName){
        return productService.getProductsByCategoryName(categoryName);
    }

    @GetMapping(value="/search/price/biggerThan/{productPrice}")
    public List<ProductDto> getProductsWhosePriceBigger(@PathVariable("productPrice") double productPrice) {
        return productService.getProductsWhosePriceBigger(productPrice);
    }
    
    @GetMapping(value="/search/price/lowerThan/{productPrice}")
    public List<ProductDto> getProductsWhosePriceLower(@PathVariable("productPrice") double productPrice) {
        return productService.getProductsWhosePriceLower(productPrice);
    }

}
