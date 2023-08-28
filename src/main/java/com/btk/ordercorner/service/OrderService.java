package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.vm.AddOrderVm;
import com.btk.ordercorner.model.vm.GetOrderDetailsVm;
import com.btk.ordercorner.model.vm.GetOrdersVm;
import com.btk.ordercorner.model.vm.SearchOrdersByDateVm;

public interface OrderService {
    List<GetOrdersVm> getAllOrderVm();
    List<GetOrdersVm> getOrderByCustomerId(int customerId);
    int addOrderVm(AddOrderVm addOrderVm);
    List<GetOrdersVm> getOrdersBeforeDate(SearchOrdersByDateVm orderVm);
    List<GetOrdersVm> getOrdersAfterDate(SearchOrdersByDateVm orderVm);
    List<GetOrderDetailsVm> getOrderDetailsById(int orderId);
    
}
