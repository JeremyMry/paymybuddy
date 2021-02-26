package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.service.IBankTransferApiServiceMock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankTransferApiServiceMockImpl implements IBankTransferApiServiceMock {

    // This method simulate a call to an api who transfer money from the bank account to the user wallet
    @Override
    public Boolean transferMoneyFromTheBankAccountMock(BigDecimal amount) {
        return true;
    }

    // This method simulate a call to an api who transfer money to the bank account from the user wallet
    @Override
    public Boolean transferMoneyToTheBankAccountMock(BigDecimal amount) {
        return true;
    }

}
