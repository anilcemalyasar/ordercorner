package com.btk.ordercorner.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.model.vm.AddPaymentTypeVm;
import com.btk.ordercorner.model.vm.GetPaymentTypeVm;
import com.btk.ordercorner.model.vm.UpdatePaymentTypeVm;
import com.btk.ordercorner.service.PaymentTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments/")
public class PaymentTypeController {
    
    private PaymentTypeService paymentTypeService;

    public PaymentTypeController(PaymentTypeService paymentTypeService) {
        this.paymentTypeService = paymentTypeService;
    }

    @GetMapping("")
    List<GetPaymentTypeVm> getAllPaymentTypeVm(){
        return paymentTypeService.getAllPaymentTypeVm();
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addPaymentType(@Valid @RequestBody AddPaymentTypeVm addPaymentTypeVm){
        paymentTypeService.addPaymentTypeVm(addPaymentTypeVm);
    }

    @PatchMapping("update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updatePaymentType(@Valid @RequestBody UpdatePaymentTypeVm updatePaymentTypeVm) {
        return paymentTypeService.updatePaymentType(updatePaymentTypeVm);
    }
}
