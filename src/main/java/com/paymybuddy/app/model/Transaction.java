package com.paymybuddy.app.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotBlank
    @Size(max = 25)
    private String reference;

    @NotBlank
    @NotNull
    @Digits(integer=5, fraction=2)
    private BigDecimal amount;

    @NotBlank
    @NotNull
    private Long creditor;

    @NotBlank
    @NotNull
    private Long debtor;

    public Transaction() {}


    public Transaction(String reference, BigDecimal amount, Long creditor, Long debtor) {
        this.reference = reference;
        this.amount = amount;
        this.creditor = creditor;
        this.debtor = debtor;
    }

    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCreditor() {
        return creditor;
    }
    public void setCreditor(Long creditor) {
        this.creditor = creditor;
    }

    public Long getDebtor() {
        return debtor;
    }
    public void setDebtor(Long debtor) {
        this.debtor = debtor;
    }
}
