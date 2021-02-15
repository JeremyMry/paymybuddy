package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Users;
import com.paymybuddy.app.DTO.UserProfile;
import com.paymybuddy.app.DTO.UserSummary;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.IUserService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Transactional
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactServiceImpl contactServiceImpl;

    @Autowired
    private BankTransferApiServiceMockImpl bankTransferApiServiceMock;

    @Autowired
    Logger logger;

    @Override
    public Users getUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public Users getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                             .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
    }
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
        Users user = getUserByEmail(email);
        return new UserProfile(user.getUsername(), user.getEmail());
    }

    @Override
    public Boolean addMoneyToTheWallet(UserPrincipal currentUser, BigDecimal sum) {
        Users user = getUser(currentUser.getId());
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

    @Override
    public Boolean removeMoneyFromTheWallet(UserPrincipal currentUser, BigDecimal sum) {
        Users user = getUser(currentUser.getId());
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

    @Override
    public void updateCreditorWallet(BigDecimal transactionAmount, Long creditorId) {
        Users creditor = getUser(creditorId);
        BigDecimal wallet = creditor.getWallet();
        creditor.setWallet(wallet.add(transactionAmount));
        userRepository.save(creditor);
    }

    @Override
    public void updateDebtorWallet(BigDecimal transactionAmount, Long debtorId) {
        Users debtor = getUser(debtorId);
        BigDecimal wallet = debtor.getWallet();
        debtor.setWallet(wallet.subtract(transactionAmount));
        userRepository.save(debtor);
    }

    @Override
    public void updatePassword(UserPrincipal currentUser, String password) {
        Users user = getUser(currentUser.getId());
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Boolean updateEmail(UserPrincipal currentUser, String newEmail) {
        if(getEmailAvailability(newEmail)) {
            Users user = getUser(currentUser.getId());
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
