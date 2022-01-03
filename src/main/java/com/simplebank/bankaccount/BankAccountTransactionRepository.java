package com.simplebank.bankaccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountTransactionRepository extends JpaRepository<BankAccountTransaction, Long> {
}