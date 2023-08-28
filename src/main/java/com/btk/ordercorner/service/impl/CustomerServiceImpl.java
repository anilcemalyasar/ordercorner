package com.btk.ordercorner.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.exception.NotFoundException;
import com.btk.ordercorner.exception.customer.CustomerAlreadyExistsException;
import com.btk.ordercorner.exception.customer.CustomerNotFoundException;
import com.btk.ordercorner.model.dto.CustomerDto;
import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.model.vm.AddCustomerVm;
import com.btk.ordercorner.model.vm.UpdatePasswordVm;
import com.btk.ordercorner.model.vm.UpdateWalletVm;
import com.btk.ordercorner.repository.CustomerRepository;
import com.btk.ordercorner.service.CustomerService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private CustomerRepository customerRepository;
    private ModelMapperManager modelMapperManager;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapperManager modelMapperManager
    ,PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.modelMapperManager = modelMapperManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "customers")
    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
        .map(customer -> modelMapperManager.forResponse().map(customer, CustomerDto.class)).collect(Collectors.toList());
    }

    @Cacheable(value = "customers", key = "#customerId")
    @Override
    public CustomerDto getByCustomerId(int customerId) {
        Authentication auth = getAuth();
        String username = customerRepository.findById(customerId).get().getUsername();
        if(!auth.getName().equals(username)) {
            logger.error("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
            throw new NotFoundException("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
        }
        if(!existsById(customerId)) {
            logger.error("Bu ID numarasına sahip bir  kullanıcı bulunmamaktadır!");
            throw new CustomerNotFoundException("Bu ID numarasına sahip bir  kullanıcı bulunmamaktadır!");
        }
        return modelMapperManager.forResponse().map(customerRepository.findById(customerId).get(), CustomerDto.class);
    }

    @CachePut(value = "customers", key = "#customerVm.customerId")
    @Override
    public int addNewCustomer(AddCustomerVm customerVm) {
        List<Customer> customers = customerRepository.findByUsernameIgnoreCase(customerVm.getUsername());
        if (!customers.isEmpty()) {
            String errorMessage = "Bu kullanıcı ismine sahip bir kullanıcı zaten sisteme kayıtlıdır!";
            logger.error(errorMessage);
            throw new CustomerAlreadyExistsException(errorMessage);
        }
        CustomerDto customerDto = modelMapperManager.forRequest().map(customerVm, CustomerDto.class);
        Customer customer = modelMapperManager.forRequest().map(customerDto, Customer.class);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        String message = customer.getCustomerFirstName() + " " + customer.getCustomerLastName() + " isimli kullanıcı sisteme eklendi!";
        logger.info(message);
        return customer.getCustomerId();

    }

    @CacheEvict(value = "customers", key = "#customerId")
    @Override
    public String deleteByCustomerId(int customerId) {
        if (!existsById(customerId)) {
            String errorMessage = customerId + " ID numarasına sahip bir kullanıcı bulunmamaktadır!";
            logger.error(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        }
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        customerRepository.deleteById(customerId);
        return customer.getCustomerFirstName() + " " + customer.getCustomerLastName()
                + " isimli kullanıcı silinmiştir!";

    }

    @Override
    public boolean existsById(int customerId) {
        return customerRepository.existsById(customerId) ? true : false;
    }

    public Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @Override
    public String updateWallet(UpdateWalletVm walletVm) {
        Customer customer = getCustomerById(walletVm.getCustomerId());
        customer.setWallet(customer.getWallet() + walletVm.getWallet());
        customerRepository.save(customer);
        return "Sayın " + customer.getCustomerFirstName() + " " + customer.getCustomerLastName() + " cüzdanınıza " + walletVm.getWallet() + " TL eklendi. Toplam Bakiyeniz: " + customer.getWallet();
    }

    private Customer getCustomerById(int customerId) {
        Authentication auth = getAuth();
        String username = customerRepository.findById(customerId).get().getUsername();
        if(!auth.getName().equals(username)) {
            logger.error("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
            throw new NotFoundException("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
        }
        Customer customer = customerRepository.findById(customerId).get();
        return customer;
    }

    @Override
    public String updatePassword(UpdatePasswordVm passwordVm) {
        Customer customer = getCustomerById(passwordVm.getCustomerId());
        customer.setPassword(passwordEncoder.encode(passwordVm.getPassword()));
        customerRepository.save(customer);
        return "Sayın " + customer.getCustomerFirstName() + " " + customer.getCustomerLastName() + " şifreniz güncellenmiştir!";
    }

    
}
