package com.react.test.dto;

import java.math.BigDecimal;

public class UserDetailsDto {
    String categoryType;
    BigDecimal currentAmount;
    BigDecimal previousAmount;

    public UserDetailsDto(String categoryType, BigDecimal currentAmount, BigDecimal previousAmount) {
        this.categoryType = categoryType;
        this.currentAmount = currentAmount;
        this.previousAmount = previousAmount;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(BigDecimal previousAmount) {
        this.previousAmount = previousAmount;
    }
}
