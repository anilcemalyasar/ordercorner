package com.btk.ordercorner.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.model.vm.AddOrderVm;
import com.btk.ordercorner.model.vm.GetOrderDetailsVm;
import com.btk.ordercorner.model.vm.GetOrdersVm;
import com.btk.ordercorner.model.vm.SearchOrdersByDateVm;
import com.btk.ordercorner.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/orders/")
@Tag(name = "Sipariş İşlemleri")
@SecurityRequirement(name = "deneme")
public class OrderController {
    
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value="")
    @Operation(
        description = "Bu metod, kullanıcıların tüm siparişlerini görüntüleme işlemlerini gerçekleştirir.",
        summary =   "Bu metod, kullanıcıların tüm siparişlerini görüntüler.",
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
    public List<GetOrdersVm> getAllOrderVm() {
        return orderService.getAllOrderVm();
    }

    @PostMapping(value="add")
    @Operation(
        description = "Bu metod, sepete ekleme işlemi tamamlananan kullanıcıların siparişinin oluşturulmasını sağlar.",
        summary =   "Bu metod, sipariş oluşturmayı sağlar.",
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
    public int addOrderVm(@Valid @RequestBody AddOrderVm addOrderVm){
        return orderService.addOrderVm(addOrderVm);
    }
    
    // sadece user kendi siparişlerini görebilecek!
    @GetMapping(value = "/{customerId}")
    @Operation(
        description = "Bu metod, geçerli kullanıcının verdiği müşteri id parametresine göre siparişleri listeler.",
        summary =   "Bu metod, geçerli kullanıcının siparişlerini görüntüler",
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
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<GetOrdersVm> getOrderByCustomerId(@PathVariable("customerId") int customerId) {
        return orderService.getOrderByCustomerId(customerId);
    }


    @GetMapping(value="/searchByDateBefore")
    @PreAuthorize("hasAuthority('ROLE_USER')")    
    public List<GetOrdersVm> getOrdersBeforeDate(@Valid @RequestBody SearchOrdersByDateVm orderVm) {
        return orderService.getOrdersBeforeDate(orderVm);
    }

    @GetMapping(value="/searchByDateAfter")
    @PreAuthorize("hasAuthority('ROLE_USER')")    
    public List<GetOrdersVm> getOrdersAfterDate(@Valid @RequestBody SearchOrdersByDateVm orderVm) {
        return orderService.getOrdersAfterDate(orderVm);
    }

    @GetMapping(value="/details/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<GetOrderDetailsVm> getOrderDetailsById(@PathVariable("orderId") int orderId) {
        return orderService.getOrderDetailsById(orderId);
    }
    
    

}
