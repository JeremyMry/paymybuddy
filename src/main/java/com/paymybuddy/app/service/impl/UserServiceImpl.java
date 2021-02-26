package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Contact;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.dto.UserProfileDto;
import com.paymybuddy.app.dto.UserSummaryDto;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.IUserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactServiceImpl contactServiceImpl;

    @Autowired
    BankTransferApiServiceMockImpl bankTransferApiServiceMock;

    @Autowired
    Logger logger;

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                             .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
    }
    @Override
    public UserSummaryDto getCurrentUser(UserPrincipal currentUser) {
        return new UserSummaryDto(currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getEmail(), currentUser.getWallet());
    }

    @Override
    public Boolean getUsernameAvailability(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public Boolean getEmailAvailability(String email) { return !userRepository.existsByEmail(email); }

    @Override
    public UserProfileDto getUserProfile(String email) {
        User user = getUserByEmail(email);
        return new UserProfileDto(user.getUsername(), user.getEmail());
    }

    @Transactional
    @Override
    public Boolean addMoneyToTheWallet(UserPrincipal currentUser, BigDecimal sum) {
        User user = getUser(currentUser.getId());
        if(sum.compareTo(BigDecimal.ZERO) <= 0.00) {
            logger.error("THE AMOUNT YOU WANT TO TRANSFER CANNOT BE INFERIOR TO 0");
            return false;
        } else if(bankTransferApiServiceMock.transferMoneyFromTheBankAccountMock(sum)) {
            BigDecimal wallet = user.getWallet();
            user.setWallet(wallet.add(sum));
            userRepository.save(user);
            return true;
        } else {
            logger.error("MONEY CANNOT BE TRANSFERRED FROM THE BANK ACCOUNT");
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean removeMoneyFromTheWallet(UserPrincipal currentUser, BigDecimal sum) {
        User user = getUser(currentUser.getId());
        BigDecimal wallet = user.getWallet().subtract(sum);
        if(sum.compareTo(BigDecimal.ZERO) <= 0.00 || wallet.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        } else if(bankTransferApiServiceMock.transferMoneyToTheBankAccountMock(sum)){
            user.setWallet(wallet);
            userRepository.save(user);
            return true;
        } else {
            logger.error("MONEY CANNOT BE TRANSFERRED TO THE BANK ACCOUNT");
            return false;
        }
    }

    @Transactional
    @Override
    public void updateCreditorWallet(BigDecimal transactionAmount, User creditor) {
        BigDecimal wallet = creditor.getWallet();
        creditor.setWallet(wallet.add(transactionAmount));
        userRepository.save(creditor);
    }

    @Transactional
    @Override
    public void updateDebtorWallet(BigDecimal transactionAmount, User debtor) {
        BigDecimal wallet = debtor.getWallet();
        debtor.setWallet(wallet.subtract(transactionAmount));
        userRepository.save(debtor);
    }

    @Transactional
    @Override
    public void updatePassword(UserPrincipal currentUser, String password) {
        User user = getUser(currentUser.getId());
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public Boolean updateEmail(UserPrincipal currentUser, String newEmail) {
        if(getEmailAvailability(newEmail)) {
            User user = getUser(currentUser.getId());
            //also change the contact email link to this userid
            if(!contactServiceImpl.getEmailAvailability(currentUser.getEmail())) {
                contactServiceImpl.updateEmail(newEmail, currentUser.getEmail());
            }
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
