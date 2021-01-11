package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer transactionId;

    @Column(nullable = false)
    private String reference;

    @Column
    private int amount;

    @ManyToOne
    @JoinColumn(name="creditor_id", referencedColumnName="userId")
    private User creditor;

    @ManyToOne
    @JoinColumn(name="debtor_id", referencedColumnName="userId")
    private User debtor;

    public Transaction() {}

    public Transaction(Integer transactionId, User creditor, User debtor, String reference, int amount) {
        this.transactionId = transactionId;
        this.creditor = creditor;
        this.debtor = debtor;
        this.reference = reference;
        this.amount = amount;
    }

    public Integer getId() { return transactionId; }
    public User getCreditor() { return creditor; }
    public User getDebtor() { return debtor; }
    public String getReference() { return reference; }
    public int getAmount() { return amount; }

    public void setId(Integer id) { this.transactionId = id; }
    public void setCreditor(User creditor) { this.creditor = creditor; }
    public void setDebtor(User debtor) { this.debtor = debtor; }
    public void setReference(String reference) { this.reference = reference; }
    public void setAmount(int amount) { this.amount = amount; }
}
