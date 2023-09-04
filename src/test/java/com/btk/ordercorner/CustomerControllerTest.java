package com.btk.ordercorner;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.btk.ordercorner.controller.CustomerController;
import com.btk.ordercorner.model.dto.CustomerDto;
import com.btk.ordercorner.repository.CustomerRepository;
import com.btk.ordercorner.service.CustomerService;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    CustomerService mockCustomerService;

    @InjectMocks
    CustomerController customerController;

    @Test
    void getAll_shouldReturnCustomerDtoList() {
        CustomerDto customerDto= new CustomerDto();
        List<CustomerDto> expected = Arrays.asList(customerDto);

        when(mockCustomerService.getAllCustomers()).thenReturn(expected);

        // when
        List<CustomerDto> actual = customerController.getAllCustomers();

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.size(), actual.size())
        );

    }
    
}
