package com.paymybuddy.app.dto;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class TransactionProceedDto {

    private Long creditor;
    private String reference;
    @Digits(integer=5, fraction=2)
    private BigDecimal amount;

    public TransactionProceedDto(Long creditor, String reference, BigDecimal amount) {
        this.creditor = creditor;
        this.reference = reference;
        this.amount = amount;
    }

    public Long getCreditor() {
        return creditor;
    }
    public void setCreditor(Long creditor) {
        this.creditor = creditor;
    }

    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
