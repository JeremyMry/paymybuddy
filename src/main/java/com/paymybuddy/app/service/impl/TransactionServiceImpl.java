package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.entity.Transaction;
import com.paymybuddy.app.entity.Users;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.TransactionProceed;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BankTransferApiServiceMockImpl bankTransferApiServiceMock;

    @Override
    public Optional<Transaction> getTransaction(Long transactionId) { return transactionRepository.findById(transactionId); }

    @Override
    public List<Transaction> getAllTransactionsMade(UserPrincipal currentUser) {
        Long debtorId = currentUser.getId();
        return transactionRepository.findAllByCurrentDebtor(debtorId);
    }

    @Override
    public List<Transaction> getAllTransactionsReceived(UserPrincipal currentUser) {
        Long creditorId = currentUser.getId();
        return transactionRepository.findAllByCurrentCreditor(creditorId);
    }

    @Override
    public Boolean transactionComputation(UserPrincipal currentUser, TransactionProceed transactionProceed) {
        Users user = userService.getUser(currentUser.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("User", "currentUser", currentUser));
        BigDecimal roundFee = (transactionProceed.getAmount().subtract(transactionProceed.getAmount().multiply(BigDecimal.valueOf(0.995))));
        if(bankTransferApiServiceMock.transferMoneyToTheBankAccountMock(roundFee)){
            BigDecimal amount = transactionProceed.getAmount().add(roundFee);
            //check if the transaction amount is inferior or equal to amount of money present on the debtor wallet
            if (transactionProceed.getAmount().compareTo(user.getWallet()) >= 0.00) {
                return false;
            } else {
                userService.updateCreditorWallet(transactionProceed.getAmount(), transactionProceed.getCreditor());
                userService.updateDebtorWallet(amount, user.getId());
                Transaction transaction = new Transaction(transactionProceed.getReference(), transactionProceed.getAmount(), transactionProceed.getCreditor(), currentUser.getId());
                createTransaction(transaction);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
