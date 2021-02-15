package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.DTO.UserProfile;
import com.paymybuddy.app.DTO.UserSummary;
import com.paymybuddy.app.security.UserPrincipal;

import java.math.BigDecimal;

public interface IUserService {

    Users getUser(Long userId);
    Users getUserByEmail(String userEmail);
    UserSummary getCurrentUser(UserPrincipal currentUser);
    Boolean getUsernameAvailability(String username);
    Boolean getEmailAvailability(String email);
    UserProfile getUserProfile(String email);
    Boolean addMoneyToTheWallet(UserPrincipal currentUser, BigDecimal wallet);
    Boolean removeMoneyFromTheWallet(UserPrincipal currentUser, BigDecimal wallet);
    void updateCreditorWallet(BigDecimal transactionAmount, Long creditorId);
    void updateDebtorWallet(BigDecimal transactionAmount, Long debtorId);
    void updatePassword(UserPrincipal currentUser, String password);
    Boolean updateEmail(UserPrincipal currentUser, String newEmail);
}
