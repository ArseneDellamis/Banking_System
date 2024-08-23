package com.BankingSystem;

@FunctionalInterface
public interface AccountOps {

    double operate(double balance, double amount);
}
