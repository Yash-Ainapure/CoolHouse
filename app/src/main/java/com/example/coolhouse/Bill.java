package com.example.coolhouse;

import java.util.List;

public class Bill {
    public String billId;
    public List<Product> products;
    public double totalAmount;
    public String paymentMode;

    public Bill() {
    }

    public Bill(String billId, List<Product> products, double totalAmount,String paymentMode) {
        this.billId = billId;
        this.products = products;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}