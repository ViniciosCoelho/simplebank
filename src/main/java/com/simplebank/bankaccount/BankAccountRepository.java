package com.simplebank.bankaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Procedure("TRANSFER_BALANCE")
    void transferBalance(Long sourceId, Long destId, Double amount);
}