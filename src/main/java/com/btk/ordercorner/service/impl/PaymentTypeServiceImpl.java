package com.btk.ordercorner.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.btk.ordercorner.model.dto.PaymentTypeDto;
import com.btk.ordercorner.model.entity.PaymentType;
import com.btk.ordercorner.model.vm.AddPaymentTypeVm;
import com.btk.ordercorner.model.vm.GetPaymentTypeVm;
import com.btk.ordercorner.model.vm.UpdatePaymentTypeVm;
import com.btk.ordercorner.repository.PaymentTypeRepository;
import com.btk.ordercorner.service.PaymentTypeService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private PaymentTypeRepository paymentTypeRepository;
    private ModelMapperManager modelMapperManager;

    public PaymentTypeServiceImpl(PaymentTypeRepository paymentTypeRepository, ModelMapperManager modelMapperManager) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.modelMapperManager = modelMapperManager;
    }

    @Override
    public List<GetPaymentTypeVm> getAllPaymentTypeVm() {
        List<PaymentType> paymentTypes = paymentTypeRepository.findAll();

        List<PaymentTypeDto> modelsResponse = paymentTypes.stream()
                .map(payment -> this.modelMapperManager.forResponse()
                .map(payment,PaymentTypeDto.class)).collect(Collectors.toList());

        List<GetPaymentTypeVm> getAllPaymentTypeVms = modelsResponse.stream()
        .map(paymentDto -> this.modelMapperManager.forResponse()
        .map(paymentDto,GetPaymentTypeVm.class)).collect(Collectors.toList());           

        return getAllPaymentTypeVms;
    }

    @Override
    public void addPaymentTypeVm(AddPaymentTypeVm addPaymentTypeVm) {
        PaymentTypeDto paymentTypeDto = this.modelMapperManager.forRequest().map(addPaymentTypeVm, PaymentTypeDto.class);
        PaymentType paymentType = this.modelMapperManager.forRequest().map(paymentTypeDto, PaymentType.class);
        paymentTypeRepository.save(paymentType);
    }

    @Override
    public String updatePaymentType(UpdatePaymentTypeVm updatePaymentTypeVm) {
        PaymentType paymentType = paymentTypeRepository.findById(updatePaymentTypeVm.getPaymentId()).get();
        paymentType.setPaymentName(updatePaymentTypeVm.getPaymentName());
        paymentTypeRepository.save(paymentType);
        return paymentType.getPaymentId() + " ID li ödeme yöntemi " + paymentType.getPaymentName() + " yöntemine güncellendi!";
    }
    
}
