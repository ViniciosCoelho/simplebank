package com.simplebank.bankaccount;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "account")
public class BankAccountController {
    private final BankAccountService bankAccountServ;

    @Autowired
    public BankAccountController(BankAccountService bankAccountServ) {
        this.bankAccountServ = bankAccountServ;
    }

    @PostMapping
    public BankAccount createBankAccount(@RequestParam("balance") Optional<Double> balance) {
        return bankAccountServ.createAccount(balance);
    }

    @PutMapping(path = "transfer/{sourceId}")
    public void transferBalance(
            @PathVariable(name = "sourceId") Long sourceId,
            @RequestParam("destId") Long destId,
            @RequestParam("amount") Double amount) {
        bankAccountServ.transferBalance(sourceId, destId, amount);
    }

    @GetMapping(path = "balance/{id}")
    public Double getBalance(@PathVariable(name = "id") Long id) {
        return bankAccountServ.getBalance(id);
    }

    @GetMapping(value="transactions/{id}")
    public List<BankAccountTransaction> getTransactions(@PathVariable(name = "id") Long id) {
        return bankAccountServ.getTransactions(id);
    }
}