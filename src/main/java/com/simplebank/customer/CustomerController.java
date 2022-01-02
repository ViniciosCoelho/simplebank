package com.simplebank.customer;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "customer")
public class CustomerController {
	private final CustomerService customerService;

	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping
	public Optional<Customer> getCustomer(@RequestParam(name = "id") Long id) {
		return customerService.getCustomer(id);
	}

	@PostMapping
	public Customer createCustomer(@Valid @RequestBody Customer customer) {
		System.out.println(customer);
		return customerService.createCustomer(customer);
	}
}