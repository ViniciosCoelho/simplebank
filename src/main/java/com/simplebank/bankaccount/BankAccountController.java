package com.simplebank.bankaccount;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    public BankAccount createBankAccount(
        @RequestParam("ownerId") Long ownerId,
        @RequestParam("balance") Optional<Double> balance) {
        return bankAccountServ.createAccount(ownerId, balance);
    }
}