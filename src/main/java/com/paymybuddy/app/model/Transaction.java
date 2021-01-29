package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer transactionId;

    @Column(nullable = false)
    private String reference;

    @Column
    private int amount;

    @Column(nullable = false)
    private String creditor;

    @Column(nullable = false)
    private String debtor;

    public Transaction() {}

    public Transaction(Integer transactionId, String creditor, String debtor, String reference, int amount) {
        this.transactionId = transactionId;
        this.creditor = creditor;
        this.debtor = debtor;
        this.reference = reference;
        this.amount = amount;
    }

    public Transaction(String creditor, String debtor, String reference, int amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.reference = reference;
        this.amount = amount;
    }

    public Integer getId() { return transactionId; }
    public String getCreditor() { return creditor; }
    public String getDebtor() { return debtor; }
    public String getReference() { return reference; }
    public int getAmount() { return amount; }

    public void setId(Integer id) { this.transactionId = id; }
    public void setCreditor(String creditor) { this.creditor = creditor; }
    public void setDebtor(String debtor) { this.debtor = debtor; }
    public void setReference(String reference) { this.reference = reference; }
    public void setAmount(int amount) { this.amount = amount; }
}
