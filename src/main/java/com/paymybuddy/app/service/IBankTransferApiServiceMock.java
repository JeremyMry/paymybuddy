package com.paymybuddy.app.service;

import java.math.BigDecimal;

public interface IBankTransferApiServiceMock {

    Boolean transferMoneyFromTheBankAccountMock(BigDecimal amount);
    Boolean transferMoneyToTheBankAccountMock(BigDecimal amount);
}
