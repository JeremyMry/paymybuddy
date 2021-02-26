package com.paymybuddy.app.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private BigDecimal wallet;

    private List<Contact> contactList;

    private List<Transaction> transactionMadeList;

    private List<Transaction> transactionReceivedList;

    public UserPrincipal(Long id, String firstName, String lastName, String username, String email, String password, BigDecimal wallet, List<Contact> contactList, List<Transaction> transactionMadeList, List<Transaction> transactionReceivedList) {
        this.id = id;
        this.firstName =firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.wallet = wallet;
        this.contactList = contactList;
        this.transactionMadeList = transactionMadeList;
        this.transactionReceivedList = transactionReceivedList;
    }

    // create an UserPrincipal from an user
    public static UserPrincipal create(User user) {

        return new UserPrincipal(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getWallet(),
                user.getContactList(),
                user.getTransactionMadeList(),
                user.getTransactionReceivedList()
        );
    }

    // create an user from an UserPrincipal
    public static User create(UserPrincipal userPrincipal) {

        return new User(
                userPrincipal.getId(),
                userPrincipal.getFirstName(),
                userPrincipal.getLastName(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                userPrincipal.getPassword(),
                userPrincipal.getWallet(),
                userPrincipal.getContactList(),
                userPrincipal.getTransactionMadeList(),
                userPrincipal.getTransactionReceivedList()
        );
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

    public String getEmail() {
        return email;
    }

    public BigDecimal getWallet() { return wallet; }

    public List<Contact> getContactList() { return contactList; }

    public List<Transaction> getTransactionMadeList() { return transactionMadeList; }

    public List<Transaction> getTransactionReceivedList() { return transactionReceivedList; }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
