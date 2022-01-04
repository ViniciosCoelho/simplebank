package com.simplebank;

import java.util.HashSet;
import java.util.Set;

import com.simplebank.bankaccount.BankAccount;
import com.simplebank.customer.Customer;
import com.simplebank.customer.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SimplebankApplication {
	public static void main(String[] args) {
		SpringApplication.run(SimplebankApplication.class, args);
	}

	@Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
		return args -> {
			Customer customer = new Customer("teste", passwordEncoder().encode("teste"));
			Set<BankAccount> accounts = new HashSet<>();
			accounts.add(new BankAccount(customer, 1000.0D));
			customer.setAccounts(accounts);
			customerRepository.save(customer);
		};
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}