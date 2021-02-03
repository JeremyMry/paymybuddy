package com.paymybuddy.app.service;

import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.model.UserProfile;
import com.paymybuddy.app.model.UserSummary;
import com.paymybuddy.app.security.UserPrincipal;

import java.util.Optional;

public interface IUserService {

    Optional<Users> getUser(Long userId);
    UserSummary getCurrentUser(UserPrincipal currentUser);
    Boolean getUsernameAvailability(String username);
    Boolean getEmailAvailability(String email);
    UserProfile getUserProfile(String email);
    /**Boolean updateWallet(UserPrincipal currentUser, Integer wallet);**/
    Boolean updatePassword(UserPrincipal currentUser, String password);
    Boolean updateEmail(UserPrincipal currentUser, String newEmail);
}
