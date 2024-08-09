package com.BankingSystem.service;


import com.BankingSystem.account.Account;
import com.BankingSystem.account.AccountRepository;
import com.BankingSystem.account.AccountUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final AccountUserRepository accountUserRepo;

    public List<Account> getUserAccounts(Long userId) {
        return accountRepo.findByAccountUserId(userId);
    }

    public Account getAccountDetails(UUID accountNr) {
        return accountRepo.findById(accountNr).orElseThrow(() ->
                new RuntimeException("Account not found"));
    }

    public double checkBalance(UUID accountNr) {
        Account account = getAccountDetails(accountNr);
        double balance = account.getBalance();
        return balance;
    }

    @Transactional
    public Account deposit(UUID accountNr, double amount) {
        Account account = getAccountDetails(accountNr);
        account.setBalance(account.getBalance() + amount);
        return accountRepo.save(account);
    }

    @Transactional
    public Account withdraw(UUID accountNr, double amount) {
        Account account = getAccountDetails(accountNr);
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepo.save(account);
    }
}
