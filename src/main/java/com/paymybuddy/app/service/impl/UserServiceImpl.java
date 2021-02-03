package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.model.UserProfile;
import com.paymybuddy.app.model.UserSummary;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.IUserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactServiceImpl contactServiceImpl;

    @Autowired
    Logger logger;

    @Override
    public Optional<Users> getUser(Long userId) { return userRepository.findById(userId); }

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        return new UserSummary(currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getWallet());
    }

    @Override
    public Boolean getUsernameAvailability(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public Boolean getEmailAvailability(String email) { return !userRepository.existsByEmail(email); }

    @Override
    public UserProfile getUserProfile(String email) {
        Users user = userRepository.findByEmail(email)
                     .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return new UserProfile(user.getUsername(), user.getEmail());
    }

    /**@Override
    public Boolean updateWallet(UserPrincipal currentUser, Integer wallet) {
        Users user = userRepository.findById(currentUser.getId())
                     .orElseThrow(() -> new ResourceNotFoundException("User", "currentUser", currentUser));
        user.setWallet(wallet);
        userRepository.save(user);
        return true;
    }**/

    @Override
    public Boolean updatePassword(UserPrincipal currentUser, String password) {
        Users user = userRepository.findById(currentUser.getId())
                                   .orElseThrow(() -> new ResourceNotFoundException("User", "currentUser", currentUser));
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean updateEmail(UserPrincipal currentUser, String newEmail) {
        if(getEmailAvailability(newEmail)) {
            Users user = userRepository.findById(currentUser.getId())
                                       .orElseThrow(() -> new ResourceNotFoundException("User", "currentUser", currentUser));
            //also change the contact email link to this userid
            contactServiceImpl.updateEmail(newEmail, currentUser.getEmail());
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

}
