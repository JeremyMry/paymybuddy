package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.entity.Transaction;
import com.paymybuddy.app.model.TransactionProceed;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCalculatorImpl transactionCalculator;

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

    @Transactional
    @Override
    public Boolean createTransaction(UserPrincipal currentUser, TransactionProceed transactionProceed) {
        if (transactionProceed.getAmount() <= 0) {
            return false;
        } else {
            Transaction transaction = new Transaction(transactionProceed.getReference(), transactionProceed.getAmount(), transactionProceed.getCreditor(), currentUser.getId());
            transactionRepository.save(transaction);
            return true;
         /**
            Users debtor = userService.getProfile(transaction.getDebtor().getId()).get();
            Users creditor = userService.getProfile(transaction.getCreditor().getId()).get();
            Integer creditorWallet = creditor.getWallet() + transaction.getAmount();
            Integer debtorWallet = debtor.getWallet()  - transaction.getAmount();

            if (debtorWallet < 0) {
                return false;
            } else {
                //userService.updateWallet(debtorWallet, debtor.getId());
                //userService.updateWallet(creditorWallet, creditor.getId());
                transactionRepository.save(transaction);
                return true;
            }**/
        }
    }
}
