package com.simplebank.bankaccount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BankAccountTransactionRepository extends JpaRepository<BankAccountTransaction, Long> {
    @Query("SELECT t FROM BankAccountTransaction t WHERE t.sourceAccount.id = :id or t.destAccount.id = :id")
    List<BankAccountTransaction> findAllTransactionsById(@Param("id") Long id);
}