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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(BankAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public BankAccount getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(BankAccount destAccount) {
        this.destAccount = destAccount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((destAccount == null) ? 0 : destAccount.hashCode());
        result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((sourceAccount == null) ? 0 : sourceAccount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BankAccountTransaction other = (BankAccountTransaction) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (destAccount == null) {
            if (other.destAccount != null)
                return false;
        } else if (!destAccount.equals(other.destAccount))
            return false;
        if (finishTime == null) {
            if (other.finishTime != null)
                return false;
        } else if (!finishTime.equals(other.finishTime))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (sourceAccount == null) {
            if (other.sourceAccount != null)
                return false;
        } else if (!sourceAccount.equals(other.sourceAccount))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BankAccountTransaction [amount=" + amount + ", destAccount=" + destAccount + ", finishTime="
                + finishTime + ", id=" + id + ", sourceAccount=" + sourceAccount + "]";
    }
}