package com.react.test.dto;

import java.sql.Timestamp;

public class StatementResponseDto {
    private String id;
    private Timestamp time;
    private String description;
    private int mcc;
    private boolean hold;
    private Double amount;
    private Double operationAmount;
    private int currencyCode;
    private Double commissionRate;
    private Double cashbackAmount;
    private Double balance;
    private CategoryType categoryType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(Double operationAmount) {
        this.operationAmount = operationAmount;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Double getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(Double cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
