package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.dto.AddressDto;
import com.btk.ordercorner.model.dto.CustomerDto;
import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.vm.AddAddressVm;
import com.btk.ordercorner.model.vm.AddCustomerVm;
import com.btk.ordercorner.model.vm.RemoveProductFromFavoritesVm;
import com.btk.ordercorner.model.vm.UpdatePasswordVm;
import com.btk.ordercorner.model.vm.UpdateWalletVm;

public interface CustomerService {
    
    List<CustomerDto> getAllCustomers();
    CustomerDto getByCustomerId(int customerId);
    int addNewCustomer(AddCustomerVm customerVm);
    String deleteByCustomerId(int customerId);
    boolean existsById(int customerId);
    String updatePassword(UpdatePasswordVm passwordVm);
    String updateWallet(UpdateWalletVm walletVm);
    List<ProductDto> getFavoriteProductsOfCustomer(int customerId);
    String addProductToFavorites(int customerId, int productId);
    String removeProductFromFavorites(int customerId, RemoveProductFromFavoritesVm productVm);
    List<AddressDto> getAllAddressesByCustomerId(int customerId);
    String addAddressToCustomer(int customerId, AddAddressVm addressVm);
}
