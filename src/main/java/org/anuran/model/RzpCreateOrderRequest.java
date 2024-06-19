package org.anuran.model;

public class RzpCreateOrderRequest {
    double amount;
    String currency;
    String receipt;

    public RzpCreateOrderRequest(double amount, String currency, String receipt) {
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
