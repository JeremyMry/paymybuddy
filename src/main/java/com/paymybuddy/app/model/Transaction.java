package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer transactionId;

    @Column(length = 64, nullable = false)
    private String creditor;

    @Column(length = 64, nullable = false)
    private String debtor;

    @Column(nullable = false)
    private String reference;

    @Column
    private int amount;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="userId")
    private User user;

    public Transaction() {}

    public Transaction(Integer transactionId, String creditor, String debtor, String reference, int amount, User user) {
        this.transactionId = transactionId;
        this.creditor = creditor;
        this.debtor = debtor;
        this.reference = reference;
        this.amount = amount;
        this.user = user;
    }

    public Integer getId() { return transactionId; }
    public String getCreditor() { return creditor; }
    public String getDebtor() { return debtor; }
    public String getReference() { return reference; }
    public int getAmount() { return amount; }
    public User getUser() { return user; }

    public void setId(Integer id) { this.transactionId = id; }
    public void setCreditor(String creditor) { this.creditor = creditor; }
    public void setDebtor(String debtor) { this.debtor = debtor; }
    public void setReference(String reference) { this.reference = reference; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setUser(User user) { this.user = user; }
}
