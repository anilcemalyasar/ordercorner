package com.btk.ordercorner.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.btk.ordercorner.config.CustomerUserDetails;
import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.repository.CustomerRepository;

@Component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByUsername(username);
        return customer.map(CustomerUserDetails::new)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    
}
