package com.btk.ordercorner.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.btk.ordercorner.exception.NotFoundException;
import com.btk.ordercorner.model.dto.AddressDto;
import com.btk.ordercorner.model.entity.Address;
import com.btk.ordercorner.model.vm.AddAddressVm;
import com.btk.ordercorner.repository.AddressRepository;
import com.btk.ordercorner.service.AddressService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;
    private ModelMapperManager modelMapperManager;

    public AddressServiceImpl(AddressRepository addressRepository,
                            ModelMapperManager modelMapperManager) {
        this.addressRepository = addressRepository;
        this.modelMapperManager = modelMapperManager;
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapperManager.forRequest().map(address, AddressDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto getAddressById(int addressId) {
        if(!existsById(addressId)) {
            throw new NotFoundException(addressId + " id numaralı bir adres bulunamadı!");
        }
        Address address = addressRepository.findById(addressId).get();
        return modelMapperManager.forRequest().map(address, AddressDto.class);
    }

    @Override
    public String addNewAddress(AddAddressVm addressVm) {
        AddressDto addressDto = modelMapperManager.forRequest().map(addressVm, AddressDto.class);
        Address address = modelMapperManager.forRequest().map(addressDto, Address.class);
        addressRepository.save(address);
        return address.getAddressId() + " ID numaralı adres sisteme eklendi!";
    }

    @Override
    public String deleteAddressById(int addressId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAddressById'");
    }

    @Override
    public boolean existsById(int addressId) {
        Optional<Address> optional = addressRepository.findById(addressId);
        return optional.isPresent() ? true : false;
    }
    
}
