package com.btk.ordercorner.service;

import java.util.List;

import com.btk.ordercorner.model.dto.AddressDto;
import com.btk.ordercorner.model.vm.AddAddressVm;

public interface AddressService {
    
    List<AddressDto> getAllAddresses();

    AddressDto getAddressById(int addressId);
    
    String addNewAddress(AddAddressVm addressVm);

    String deleteAddressById(int addressId);

    boolean existsById(int addressId);

}
