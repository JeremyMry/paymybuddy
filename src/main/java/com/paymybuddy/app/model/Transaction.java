package com.paymybuddy.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@JsonIgnoreProperties({"creator", "debtor", "creditor"})
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("transactionReceivedList")
    @JoinColumn(name = "creditor_id", referencedColumnName="id")
    private User creditor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("transactionMadeList")
    @JoinColumn(name = "debtor_id", referencedColumnName="id")
    private User debtor;

    public Transaction() {}

    public Transaction(String reference, BigDecimal amount, User creditor, User debtor) {
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

    public User getCreditor() {
        return creditor;
    }
    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public User getDebtor() {
        return debtor;
    }
    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }
}
