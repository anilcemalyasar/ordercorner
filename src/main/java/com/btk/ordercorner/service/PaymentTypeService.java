package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.vm.AddPaymentTypeVm;
import com.btk.ordercorner.model.vm.GetPaymentTypeVm;
import com.btk.ordercorner.model.vm.UpdatePaymentTypeVm;

public interface PaymentTypeService {
    List<GetPaymentTypeVm> getAllPaymentTypeVm();

    void addPaymentTypeVm(AddPaymentTypeVm addPaymentTypeVm);

    String updatePaymentType(UpdatePaymentTypeVm updatePaymentTypeVm);
}
