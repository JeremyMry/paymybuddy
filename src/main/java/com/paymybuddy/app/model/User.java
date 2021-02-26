package com.paymybuddy.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 40)
    private String firstName;

    @NotNull
    @Size(max = 40)
    private String lastName;

    @NotNull
    @Size(max = 40)
    private String username;

    @NotNull
    @Size(max = 40)
    @Email
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Digits(integer=5, fraction=2)
    private BigDecimal wallet;

    @OneToMany(targetEntity = Contact.class, cascade = CascadeType.REMOVE, mappedBy="creator")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("creator")
    private List<Contact> contactList = new ArrayList<>();

    @OneToMany(targetEntity = Transaction.class, cascade = CascadeType.REMOVE, mappedBy="debtor")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("debtor")
    private List<Transaction> transactionMadeList = new ArrayList<>();

    @OneToMany(targetEntity = Transaction.class, cascade = CascadeType.REMOVE, mappedBy="creditor")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("creditor")
    private List<Transaction> transactionReceivedList = new ArrayList<>();

    public User() {}

    public User(String firstName, String lastName, String username, String email, String password, BigDecimal wallet, List<Contact> contactList, List<Transaction> transactionMadeList, List<Transaction> transactionReceivedList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
        this.contactList = contactList;
        this.transactionMadeList = transactionMadeList;
        this.transactionReceivedList = transactionReceivedList;
    }

    public User(Long id, String firstName, String lastName, String username, String email, String password, BigDecimal wallet, List<Contact> contactList, List<Transaction> transactionMadeList, List<Transaction> transactionReceivedList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
        this.contactList = contactList;
        this.transactionMadeList = transactionMadeList;
        this.transactionReceivedList = transactionReceivedList;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getWallet() {
        return wallet;
    }
    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public List<Contact> getContactList() { return contactList; }

    public List<Transaction> getTransactionMadeList() { return transactionMadeList; }

    public List<Transaction> getTransactionReceivedList() { return transactionReceivedList; }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
