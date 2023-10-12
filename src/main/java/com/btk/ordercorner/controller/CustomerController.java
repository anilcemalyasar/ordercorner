package com.btk.ordercorner.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.model.dto.CustomerDto;
import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.model.vm.AddAddressVm;
import com.btk.ordercorner.model.vm.AddCustomerVm;
import com.btk.ordercorner.model.vm.RemoveProductFromFavoritesVm;
import com.btk.ordercorner.model.vm.UpdatePasswordVm;
import com.btk.ordercorner.model.vm.UpdateWalletVm;
import com.btk.ordercorner.service.CustomerService;
import com.btk.ordercorner.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/customers/")
@Tag(name = "Müşteri İşlemleri")
@SecurityRequirement(name = "deneme")
public class CustomerController {
    
    private CustomerService customerService;
    private ReportService reportService;

    public CustomerController(CustomerService customerService, ReportService reportService) {
        this.customerService = customerService;
        this.reportService = reportService;
    }

    @Operation(
        description = "Bu metod, sadece \"Admin\" yetkisine sahip kullanıcıların sistemde kayıtlı tüm müşterileri listelemesi için kullanılır. \"Admin\" kullancıları, bu metod sayesinde tüm müşteri verilerine erişebilir",
        summary =   "Bu metod, sistemde kayıtlı tüm müşterileri listelemesi için kullanılır.",
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
    @GetMapping(value="")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(
        description = "Bu metod, yalnızca \"Admin\" yetkisine sahip kullanıcıların, belirtilen kullanıcının {id} parametresine göre sistemde kayıtlı kullanıcı verilerini getirmesi için kullanılır. \"Admin\" kullanıcıları, bu metod aracılığıyla belirli bir kullanıcıların detaylı bilgilerine erişebilir",
        summary =   "Bu metod, {id} parametresi gönderilen kullanıcıyı listelemek için kullanılır.",
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
    @GetMapping(value="/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CustomerDto getByCustomerId(@PathVariable("customerId") int customerId) {
        return customerService.getByCustomerId(customerId);
    }
    
    @Operation(
        description = "Bu metod, sistemde yeni bir kullanıcı hesabı oluşturmak amacıyla kullanılır. Kullanıcıların temel bilgilerini alarak, sisteme kayıt yapabilmesini sağlar.",
        summary =   "Bu metod, sisteme yeni bir kullanıcı ekler. ",
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
    @PostMapping(value="add")
    public int addNewCustomer(@Valid @RequestBody AddCustomerVm customerVm) {
        return customerService.addNewCustomer(customerVm);
    }
    
    
    @DeleteMapping("/{customerId}")
    @Operation(
        description = "Bu metod, sistemde yeni bir kullanıcı hesabı oluşturmak amacıyla kullanılır. Kullanıcıların temel bilgilerini alarak, sisteme kayıt yapabilmesini sağlar.",
        summary =   "Bu metod, \"Admin\" rolüne sahip kullanıcının belirtilen {id} parametresine göre müşteriyi siler.",
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteByCustomerId(@PathVariable("customerId") int customerId) {
        return customerService.deleteByCustomerId(customerId);
    }

    @Operation(
        description = "Bu metod, sistemde {id} numaralı kullanıcın bakiyesini güncellemesine olanak sağlar.",
        summary =   "Bu metod, \"User\" rolüne sahip kullanıcının belirtilen {id} parametresine göre kendi cüzdan bakiyesini güncelleyebilir.",
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
    @PutMapping(value="/updateWallet")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String updateWalletByCustomerId(@Valid @RequestBody UpdateWalletVm walletVm) {
        return customerService.updateWallet(walletVm);
    }

    @Operation(
        description = "Bu metod, sistemde {id} numaralı kullanıcın şifresini değiştirebilmesine olanak sağlar.",
        summary =   "Bu metod, \"User\" rolüne sahip kullanıcının belirtilen {id} parametresine göre kendi şifresini güncelleyebilir.",
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
    @PutMapping(value="/changePassword")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String updatePasswordByCustomerId(@Valid @RequestBody UpdatePasswordVm passwordVm) {
        return customerService.updatePassword(passwordVm);
    }

    @GetMapping(value="/report/{format}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String generateReport(@PathVariable("format") String format) throws FileNotFoundException, JRException {
        return reportService.exportReportCustomer(format);
    }

    @Operation(
        summary = "Bu metod, sistemde {customerId} numaralı müşterinin favorilediği ürünleri görmesine olanak sağlar.",
        description = "Bu metod, ortak tablo olarak birleştirilen musteri_favori_urunler tablosundan id numarası verilmiş müşterinin favorilediği ürünleri bir liste olarak görmesini sağlar."
    )
    @GetMapping(value="/{customerId}/products/favorites")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<ProductDto> getFavoriteProductsOfCustomer(@PathVariable("customerId") int customerId) {
        return customerService.getFavoriteProductsOfCustomer(customerId);
    }


    @Operation(
        description = "Bu metod, sistemde {customerId} numaralı müşterinin favorilerine ürün eklemesine olanak sağlar",
        summary = "Bu metod, sistemde {customerId} numaralı müşterinin favorilerine ürün eklemesine olanak sağlar",
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
    @PostMapping(value="/{customerId}/favorites/products/{productId}/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String addProductToFavorites(@PathVariable("customerId") int customerId,
                                        @PathVariable("productId") int productId) {
        return customerService.addProductToFavorites(customerId, productId);
    }

    @Operation(
        description = "Bu metod, sistemde {customerId} numaralı müşterinin favorilediği ürünü listeden kaldırmasına olanak sağlar",
        summary = "Bu metod, sistemde {customerId} numaralı müşterinin favorilediği ürünü listeden kaldırmasına olanak sağlar"
    )
    @DeleteMapping(value="/{customerId}/favorites/products/remove")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String removeProductFromFavorites(@PathVariable("customerId") int customerId, 
                                    @Valid @RequestBody RemoveProductFromFavoritesVm productVm) {
        return customerService.removeProductFromFavorites(customerId, productVm);
    }

    @Operation(
        description = "Bu metod, müşterinin sisteme yeni bir adresini eklemesine olanak sağlar. Oturduğu evden taşınması, iş yeri adresini kaydetmesi veya bir arkadaşının adresini ekleme gibi senaryolar için etkili bir çözüm!",
        summary = "Bu metod, {customerId} numaralı müşterinin sisteme yeni bir adresini eklemesine yarar."
    )
    @PostMapping(value="/{customerId}/addresses/add")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> addAddressToCustomer(@PathVariable("customerId") int customerId,
                                @Valid @RequestBody AddAddressVm addressVm){
        return ResponseEntity.ok(customerService.addAddressToCustomer(customerId, addressVm));
    }
    
    

}
