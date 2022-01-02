package com.simplebank.customer;

import java.util.Optional;

import com.simplebank.customer.exceptions.CustomerHandlingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
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

        Optional<Customer> dbCustomer =  customerRepository.findByUsername(username);

        if (dbCustomer.isPresent()) {
            throw new CustomerHandlingException("User already exists!");
        }

        return customerRepository.save(customer);
    }
}