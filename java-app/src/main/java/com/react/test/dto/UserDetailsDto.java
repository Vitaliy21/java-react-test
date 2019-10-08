package com.react.test.dto;

import java.math.BigDecimal;

public class UserDetailsDto {
    CategoryType categoryType;
    BigDecimal currentAmount;
    BigDecimal previousAmount;

    public UserDetailsDto(CategoryType categoryType, BigDecimal currentAmount, BigDecimal previousAmount) {
        this.categoryType = categoryType;
        this.currentAmount = currentAmount;
        this.previousAmount = previousAmount;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
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
