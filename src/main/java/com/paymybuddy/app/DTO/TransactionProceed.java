package com.paymybuddy.app.DTO;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class TransactionProceed {

    private Long creditor;
    private String reference;
    @Digits(integer=5, fraction=2)
    private BigDecimal amount;

    public TransactionProceed(Long creditor, String reference, BigDecimal amount) {
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
