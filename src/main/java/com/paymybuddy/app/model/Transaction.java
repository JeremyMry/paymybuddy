package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(length = 64, nullable = false)
    private String creditor;

    @Column(length = 64, nullable = false)
    private String debtor;

    @Column(nullable = false)
    private String reference;

    @Column
    private int amount;

    public Long getId() { return id; }
    public String getCreditor() { return creditor; }
    public String getDebtor() { return debtor; }
    public String getReference() { return reference; }
    public int getAmount() { return amount; }

    public void setId(Long id) { this.id = id; }
    public void setCreditor(String creditor) { this.creditor = creditor; }
    public void setDebtor(String debtor) { this.debtor = debtor; }
    public void setReference(String reference) { this.reference = reference; }
    public void setAmount(int amount) { this.amount = amount; }
}
