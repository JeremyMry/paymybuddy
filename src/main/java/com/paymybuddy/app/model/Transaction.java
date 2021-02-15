package com.paymybuddy.app.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long transactionId;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Long creditor;

    @Column(nullable = false)
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
