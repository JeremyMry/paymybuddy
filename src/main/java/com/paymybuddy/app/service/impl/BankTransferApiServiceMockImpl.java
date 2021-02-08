package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.service.IBankTransferApiServiceMock;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class BankTransferApiServiceMockImpl implements IBankTransferApiServiceMock {

    @Override
    public Boolean transferMoneyFromTheBankAccountMock(BigDecimal amount) {
        return true;
    }

    @Override
    public Boolean transferMoneyToTheBankAccountMock(BigDecimal amount) {
        return true;
    }

}
