package com.paymybuddy.app.model;

import javax.persistence.*;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(length = 64, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Token() {
    }

    public Token(Integer tokenId, String token, User user) {
        this.tokenId = tokenId;
        this.token = token;
        this.user = user;
    }

    public Integer getTokenId() { return tokenId; }
    public void setTokenId(Integer tokenId) { this.tokenId = tokenId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
