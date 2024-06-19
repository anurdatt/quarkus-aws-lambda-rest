package org.anuran.model;

public class RzpCreateOrderResponse {

    double amount;
    double amountDue;
    double amountPaid;
    String orderId;
    String receiptId;

    public RzpCreateOrderResponse(double amount, double amountDue,
                                  double amountPaid, String orderId,
                                  String receiptId) {
        this.amount = amount;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.orderId = orderId;
        this.receiptId = receiptId;
    }

    public RzpCreateOrderResponse() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
}
