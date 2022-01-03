package com.simplebank.bankaccount;

import java.util.Optional;

import com.simplebank.bankaccount.exceptions.BankAccountHandlingException;
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

    public void transferBalance(Long sourceId, Long destId, Double amount) {
        if (sourceId == destId) {
            throw new BankAccountHandlingException("Can't transfer to the same account!");
        }

        Optional<BankAccount> bankAccount = bankAccountRepo.findById(sourceId);

        if (!bankAccount.isPresent()) {
            throw new BankAccountHandlingException("Invalid account!");
        }

        Optional<BankAccount> tBankAccount = bankAccountRepo.findById(destId);

        if (!tBankAccount.isPresent()) {
            throw new BankAccountHandlingException("Invalid transfer account!");
        }

        BankAccount account = bankAccount.get();
        Double balance = account.getBalance();

        if (balance < amount) {
            throw new BankAccountHandlingException("Not enought balance!");
        }

        bankAccountRepo.transferBalance(sourceId, destId, amount);
    }
}