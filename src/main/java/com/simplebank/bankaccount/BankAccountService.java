package com.simplebank.bankaccount;

import java.util.Optional;

import com.simplebank.customer.Customer;
import com.simplebank.customer.CustomerService;
import com.simplebank.customer.exceptions.CustomerHandlingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepo;
    private final CustomerService customerService;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepo, CustomerService customerService) {
        this.bankAccountRepo = bankAccountRepo;
        this.customerService = customerService;
    }

    public BankAccount createAccount(Long ownerId, Optional<Double> balance) {
        Double actualBalance = 0.0D;

        if (balance.isPresent()) {
            actualBalance = balance.get();
        }

        Optional<Customer> customer = customerService.getCustomer(ownerId);

        if (!customer.isPresent()) {
            throw new CustomerHandlingException("Invalid owner!");
        }
        
        BankAccount account = new BankAccount(customer.get(), actualBalance);
        return bankAccountRepo.save(account);
    }
}