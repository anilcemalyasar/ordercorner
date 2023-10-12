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
import com.btk.ordercorner.model.dto.AddressDto;
import com.btk.ordercorner.model.dto.CustomerDto;
import com.btk.ordercorner.model.dto.ProductDto;
import com.btk.ordercorner.model.entity.Address;
import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.model.vm.AddCustomerVm;
import com.btk.ordercorner.model.vm.RemoveProductFromFavoritesVm;
import com.btk.ordercorner.model.vm.UpdatePasswordVm;
import com.btk.ordercorner.model.vm.UpdateWalletVm;
import com.btk.ordercorner.repository.AddressRepository;
import com.btk.ordercorner.repository.CustomerRepository;
import com.btk.ordercorner.repository.ProductRepository;
import com.btk.ordercorner.service.CustomerService;
import com.btk.ordercorner.util.mapper.ModelMapperManager;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private CustomerRepository customerRepository;
    private ModelMapperManager modelMapperManager;
    private ProductRepository productRepository;
    private AddressRepository addressRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapperManager modelMapperManager
    ,PasswordEncoder passwordEncoder, ProductRepository productRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.modelMapperManager = modelMapperManager;
        this.passwordEncoder = passwordEncoder;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
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

    // Mock Data ile Test için eklenen metotlar!
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getById(int id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public List<ProductDto> getFavoriteProductsOfCustomer(int customerId) {
        Authentication auth = getAuth();
        Customer customer = customerRepository.findById(customerId).get();
        String username = customer.getUsername();
        if(!auth.getName().equals(username)) {
            logger.error("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
            throw new NotFoundException("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
        }

        List<ProductDto> favProducts = customer.getFavoriteProducts()
                                    .stream()
                                .map(product -> modelMapperManager.forResponse().map(product, ProductDto.class))
                                    .collect(Collectors.toList());
        return favProducts;
    }

    @Override
    public String addProductToFavorites(int customerId, int productId) {
        if(!existsById(customerId)) {
            String errorMessage = customerId + " ID numarasına sahip bir kullanıcı bulunmamaktadır!";
            logger.error(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        }
        Customer customer = customerRepository.findById(customerId).get();
        List<Product> favProducts = customer.getFavoriteProducts();
        Product product = productRepository.findById(productId).get();
        favProducts.add(product);
        customer.setFavoriteProducts(favProducts);
        customerRepository.save(customer);
        return customer.getCustomerFirstName() + " adlı müşteri favorilerine " 
            + product.getProductName() + " ürününü ekledi!";
    }

    @Override
    public String removeProductFromFavorites(int customerId, RemoveProductFromFavoritesVm productVm) {
        if(!existsById(customerId)) {
            String errorMessage = customerId + " ID numarasına sahip bir kullanıcı bulunmamaktadır!";
            logger.error(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        }

        Authentication auth = getAuth();
        Customer customer = customerRepository.findById(customerId).get();
        String username = customer.getUsername();
        if(!auth.getName().equals(username)) {
            logger.error("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
            throw new NotFoundException("Başka kullanıcının bilgilerine erişim hakkınız yoktur!");
        }

        Product product = productRepository.findByProductNameIgnoreCase(productVm.getProductName());
        List<Product> favProducts = customer.getFavoriteProducts();
        favProducts.remove(product);
        // Update favorite products of customer after removing given product
        customer.setFavoriteProducts(favProducts);
        customerRepository.save(customer);
        return customer.getCustomerFirstName() + " adlı müşteri favorilerinden " 
            + product.getProductName() + " ürününü çıkardı!";

    }

    // Buraya authentication koy
    @Override
    public List<AddressDto> getAllAddressesByCustomerId(int customerId) {
        if(!existsById(customerId)) {
            String errorMessage = customerId + " ID numarasına sahip bir kullanıcı bulunmamaktadır!";
            logger.error(errorMessage);
            throw new CustomerNotFoundException(errorMessage);
        } 
        List<Address> addresses = customerRepository.findById(customerId).get()
                                    .getAddresses();
        return addresses.stream()
            .map(address -> modelMapperManager.forResponse().map(address, AddressDto.class))
            .collect(Collectors.toList());
    }

    // müşteri adres ekleyebilsin, adres silebilsin

}
