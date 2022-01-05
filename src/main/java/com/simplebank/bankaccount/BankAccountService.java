package com.simplebank.bankaccount;

import java.util.List;
import java.util.Optional;

import com.simplebank.bankaccount.exceptions.BankAccountHandlingException;
import com.simplebank.customer.Customer;
import com.simplebank.customer.CustomerRepository;
import com.simplebank.customer.exceptions.CustomerHandlingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public BankAccount createAccount(Optional<Double> balance) {
        Double actualBalance = 0.0D;

        if (balance.isPresent()) {
            actualBalance = balance.get();

            if (actualBalance < 0.0D) {
                actualBalance = 0.0D;
            }
        }

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepo.findByUsername(username);

        if (customer == null) {
            throw new CustomerHandlingException("Invalid owner!");
        }
        
        BankAccount account = new BankAccount(customer, actualBalance);
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

        BankAccount account = bankAccount.get();
        checkAccountOwnership(account);

        Boolean destAccountExists = bankAccountRepo.existsById(destId);

        if (!destAccountExists) {
            throw new BankAccountHandlingException("Invalid transfer account!");
        }

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

        BankAccount account = bankAccount.get();
        checkAccountOwnership(account);

        return bankAccount.get().getBalance();
    }

    public List<BankAccountTransaction> getTransactions(Long id) {
        Optional<BankAccount> bankAccount = bankAccountRepo.findById(id);

        if (!bankAccount.isPresent()) {
            throw new BankAccountHandlingException("Invalid account!");
        }

        BankAccount account = bankAccount.get();
        checkAccountOwnership(account);

        return accTransactionsRepo.findAllTransactionsById(id);
    }

    private void checkAccountOwnership(BankAccount bankAccount) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepo.findByUsername(username);
        Long ownerId = bankAccount.getId();

        if (ownerId != customer.getId()) {
            throw new BankAccountHandlingException("Invalid account!");
        }
    }
}