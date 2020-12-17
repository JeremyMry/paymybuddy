package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
@Table(name="transaction")
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name="Id", nullable = false)
    private Long id;

    @Column(name="creditor", length = 64, nullable = false)
    private String creditor;

    @Column(name="debtor", length = 64, nullable = false)
    private String debtor;

    @Column(name="reference", nullable = false)
    private String reference;

    @Column(name="amount")
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
