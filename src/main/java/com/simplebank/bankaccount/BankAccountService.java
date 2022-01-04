package com.simplebank.bankaccount;

import java.util.List;
import java.util.Optional;

import com.simplebank.bankaccount.exceptions.BankAccountHandlingException;
import com.simplebank.customer.Customer;
import com.simplebank.customer.CustomerRepository;
import com.simplebank.customer.exceptions.CustomerHandlingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO This will need an update to get the user from the JWT Token.
@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepo;
    private final CustomerRepository customerRepo;
    private final BankAccountTransactionRepository accTransactionsRepo;

    @Autowired
    public BankAccountService(
        BankAccountRepository bankAccountRepo,
        CustomerRepository customerRepo,
        BankAccountTransactionRepository accTransactionsRepo) {
        this.bankAccountRepo = bankAccountRepo;
        this.customerRepo = customerRepo;
        this.accTransactionsRepo = accTransactionsRepo;
    }

    public BankAccount createAccount(Long ownerId, Optional<Double> balance) {
        Double actualBalance = 0.0D;

        if (balance.isPresent()) {
            actualBalance = balance.get();
        }

        Optional<Customer> customer = customerRepo.findById(ownerId);

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

        if (amount <= 0.0D) {
            throw new BankAccountHandlingException("Transference values must be bigger than 0!");
        }

        Optional<BankAccount> bankAccount = bankAccountRepo.findById(sourceId);

        if (!bankAccount.isPresent()) {
            throw new BankAccountHandlingException("Invalid account!");
        }

        Boolean destAccountExists = bankAccountRepo.existsById(destId);

        if (!destAccountExists) {
            throw new BankAccountHandlingException("Invalid transfer account!");
        }

        BankAccount account = bankAccount.get();
        Double balance = account.getBalance();

        if (balance < amount) {
            throw new BankAccountHandlingException("Not enought balance!");
        }

        bankAccountRepo.transferBalance(sourceId, destId, amount);
    }

    public Double getBalance(Long id) {
        Optional<BankAccount> bankAccount = bankAccountRepo.findById(id);

        if (!bankAccount.isPresent()) {
            throw new BankAccountHandlingException("Invalid account!");
        }

        return bankAccount.get().getBalance();
    }

    public List<BankAccountTransaction> getTransactions(Long id) {
        Boolean accountExists = bankAccountRepo.existsById(id);

        if (!accountExists) {
            throw new BankAccountHandlingException("Invalid account!");
        }

        return accTransactionsRepo.findAllTransactionsById(id);
    }
}