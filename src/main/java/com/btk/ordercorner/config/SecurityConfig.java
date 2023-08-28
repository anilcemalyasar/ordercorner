package com.btk.ordercorner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.btk.ordercorner.service.CustomerUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }

    // sign up sayfasına permitAll() yapmamız lazım ki sayfaya giren kişiler kullanıcı kaydı oluşturabilsin
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/products/all", "/api/carts/{cartId}","/api/products/{productId}", "/api/customers/add",
                "/api/categories/all", "/api/categories/{categoryId}", "/api/products/search/{categoryName}", "/api/generate-excel-report", "/api/carts/customers/{customerId}"
                , "/api/orders/add", "/swagger-ui/index.html", "/swagger-ui/**","/v3/api-docs/**","/api/payments/",
                "/api/cartproducts/{cartId}/{productId}/{productQuantity}", "/api/cartproducts/{cartId}/{productId}", "/api/categories/report", "/api/products/search/price/biggerThan/{productPrice}",
                "/api/products/search/price/lowerThan/{productPrice}").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/api/**")
                .authenticated().and().formLogin().and().httpBasic().and().build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


}
