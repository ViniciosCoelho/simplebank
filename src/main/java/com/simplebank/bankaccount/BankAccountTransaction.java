package com.simplebank.bankaccount;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class BankAccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    // TODO change the columnDefinition to be more generic
    @Column(
        name = "finish_time",
        columnDefinition = "timestamp without time zone default (now() at time zone 'utc')",
        insertable = false,
        updatable = false,
        nullable = false
    )
    LocalDateTime finishTime;

    @Column(
        name = "amount",
        nullable = false
    )
    Double amount;

    @ManyToOne
    @JoinColumn(name = "source_id")
    BankAccount sourceAccount;

    @ManyToOne
    @JoinColumn(name = "dest_id")
    BankAccount destAccount;
}