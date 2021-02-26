package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.dto.TransactionProceedDto;
import com.paymybuddy.app.exception.ResourceNotFoundException;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.User;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.repository.UserRepository;
import com.paymybuddy.app.security.UserPrincipal;
import com.paymybuddy.app.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BankTransferApiServiceMockImpl bankTransferApiServiceMock;

    @Override
    public Transaction getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId)); }

    @Override
    public List<Transaction> getAllTransactionsMade(UserPrincipal currentUser) {
        List<Transaction> transactionMadeList = currentUser.getTransactionMadeList();
        return transactionMadeList;
    }

    @Override
    public List<Transaction> getAllTransactionsReceived(UserPrincipal currentUser) {
        List<Transaction> transactionReceivedList = currentUser.getTransactionReceivedList();
        return transactionReceivedList;
    }

    @Transactional
    @Override
    public Boolean transactionComputation(UserPrincipal currentUser, TransactionProceedDto transactionProceedDto) {
        User debtor = UserPrincipal.create(currentUser);
        User creditor = userService.getUser(transactionProceedDto.getCreditor());
        BigDecimal roundFee = (transactionProceedDto.getAmount().subtract(transactionProceedDto.getAmount().multiply(BigDecimal.valueOf(0.995))));
        BigDecimal amount = transactionProceedDto.getAmount().add(roundFee);
        Transaction transaction = new Transaction(transactionProceedDto.getReference(), transactionProceedDto.getAmount(), creditor, debtor);
        // Mock the transfer of the fee to the paymybuddy bank account
        bankTransferApiServiceMock.transferMoneyToTheBankAccountMock(roundFee);
        //check if the transaction amount is inferior or equal to amount of money present on the debtor wallet
        if (transactionProceedDto.getAmount().compareTo(debtor.getWallet()) >= 0.00) {
            return false;
        } else {
            userService.updateCreditorWallet(transactionProceedDto.getAmount(), creditor);
            userService.updateDebtorWallet(amount, debtor);
            transactionRepository.save(transaction);
            return true;
        }
    }
}
