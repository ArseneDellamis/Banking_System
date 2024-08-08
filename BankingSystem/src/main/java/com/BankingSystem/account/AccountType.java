package com.BankingSystem.account;

public enum AccountType {
    CURRENT,
    FIXED,
    SALARY,
    SAVING;
    public static AccountType fromString(String value) {
        return AccountType.valueOf(value.toUpperCase());
    }
}
