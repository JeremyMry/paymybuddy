package com.paymybuddy.app.model;

public class TransactionProceed {

    private Long creditor;
    private String reference;
    private Integer amount;

    public TransactionProceed(Long creditor, String reference, Integer amount) {
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

    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
