package com.btk.ordercorner.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.model.entity.Order;
import com.btk.ordercorner.repository.CustomerRepository;
import com.btk.ordercorner.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerServiceImpl customerServiceImpl;

    @Test
    void testfindAllCustomers() {
        // given
        List<Customer> customers = new ArrayList<Customer>();
        Customer customer1 = new Customer(
            1, "Anıl", "Yaşar", 
            "anl7", "anl7@gmail.com", "777", 
            100, "ROLE_USER", new ArrayList<Order>(), null);
        Customer customer2 = new Customer(
            2, "Aşkın", "Yaşar", 
            "askin", "askin@gmail.com", "10", 
            100000, "ROLE_USER", new ArrayList<Order>(), null);

        customers.add(customer1);
        customers.add(customer2);

        // when
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> customerList = customerServiceImpl.findAllCustomers();

        assertEquals(2, customerList.size());
        
    }

    @Test
    void test_findCustomerById() {
        // given
        Customer customer1 = new Customer(
            1, "Anıl", "Yaşar", 
            "anl7", "anl7@gmail.com", "777", 
            100, "ROLE_USER", new ArrayList<Order>(), null);

        Optional<Customer> optional = Optional.of(customer1);

        when(customerRepository.findById(customer1.getCustomerId())).thenReturn(optional);

        Customer found = customerServiceImpl.getById(1);

        assertEquals(found.getCustomerId(), customer1.getCustomerId());

    }

}
