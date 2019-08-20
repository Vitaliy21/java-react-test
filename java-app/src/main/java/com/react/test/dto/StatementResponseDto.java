package com.react.test.dto;

public class StatementResponseDto {
    private String id;
    private Long time;
    private String localTime;
    private String description;
    private int mcc;
    private boolean hold;
    private int amount;
    private int operationAmount;
    private int currencyCode;
    private int commissionRate;
    private int cashbackAmount;
    private int balance;
    private CategoryType categoryType;
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(int operationAmount) {
        this.operationAmount = operationAmount;
    }

    public int getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(int currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public int getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(int cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
