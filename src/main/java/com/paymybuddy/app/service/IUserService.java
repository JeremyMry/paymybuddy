package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.dto.UserProfileDto;
import com.paymybuddy.app.dto.UserSummaryDto;
import com.paymybuddy.app.security.UserPrincipal;

import java.math.BigDecimal;

public interface IUserService {

    User getUser(Long userId);
    User getUserByEmail(String userEmail);
    UserSummaryDto getCurrentUser(UserPrincipal currentUser);
    Boolean getUsernameAvailability(String username);
    Boolean getEmailAvailability(String email);
    UserProfileDto getUserProfile(String email);
    Boolean addMoneyToTheWallet(UserPrincipal currentUser, BigDecimal wallet);
    Boolean removeMoneyFromTheWallet(UserPrincipal currentUser, BigDecimal wallet);
    void updateCreditorWallet(BigDecimal transactionAmount, User creditor);
    void updateDebtorWallet(BigDecimal transactionAmount, User debtor);
    void updatePassword(UserPrincipal currentUser, String password);
    Boolean updateEmail(UserPrincipal currentUser, String newEmail);
}
