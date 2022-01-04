package com.simplebank.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.simplebank.customer.exceptions.CustomerHandlingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomer(Long id) {
		return customerRepository.findById(id);
	}

    public Customer createCustomer(Customer customer) {
        String username = customer.getUsername();

        if (username.isEmpty()) {
            throw new CustomerHandlingException("Null username given!");
        }

        Customer dbCustomer =  customerRepository.findByUsername(username);

        if (dbCustomer != null) {
            throw new CustomerHandlingException("User already exists!");
        }

        return customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);

        if (customer == null) {
            throw new CustomerHandlingException("User not found.");
        }

        // TODO Add user roles as database entities and assign them to each user
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        User appUser = new User(customer.getUsername(), customer.getPassword(), authorities);
        
        return appUser;
    }
}