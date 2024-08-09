package com.BankingSystem.controller;

import com.BankingSystem.account.*;
import com.BankingSystem.payLoad.AccountRegister;
import com.BankingSystem.responseHandler.Response;
import com.BankingSystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepo;
    private final AccountUserRepository accountUserRepo;
    private final AccountService accountService;

//    creating a user and associated bank account at http://localhost:8080/api/account/create
    @PostMapping("/create")
    public ResponseEntity<Response> creatingAccount(
            @RequestBody AccountRegister register
            )
    {
//        create and save the user
        Account newAccount = new Account();
        AccountUser newUser = new AccountUser();
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setEmail(register.getEmail());
        AccountUser savedUser = accountUserRepo.save(newUser);
// Validate and set the account type
        String acType = register.getAccountType().name().toUpperCase();

        AccountType accountType;
        try {
            accountType = AccountType.valueOf(acType);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid account type: " + acType);
        }

        newAccount.setAccountType(accountType);
        newAccount.setAccountUser(savedUser);
        newAccount.setPassword(register.getPassword());
        Account savedAccount = accountRepo.save(newAccount);
        Response<Account> accountResponse= new Response<>();
        accountResponse.setStatus(HttpStatus.CREATED);
        accountResponse.setMessage("account created successfully");
        return ResponseEntity.ok(accountResponse);
    }

//    fetching all account associated with the user like http://localhost:8080/api/account/user/1/accounts
    @GetMapping("/user/{userId}/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

//  fetching account details like http://localhost:8080/api/account/9a8f2710-14a6-4d2b-bdde-6f4b55941997/details
    @GetMapping("/{accountNr}/details")
    public ResponseEntity<Account> getAccountDetails(@PathVariable UUID accountNr) {
        Account account = accountService.getAccountDetails(accountNr);
        return ResponseEntity.ok(account);
    }

//    getting account balance like http://localhost:8080/api/account/9a8f2710-14a6-4d2b-bdde-6f4b55941997/balance
    @GetMapping("/{accountNr}/balance")
    public ResponseEntity<Double> checkBalance(@PathVariable UUID accountNr) {
        double balance = accountService.checkBalance(accountNr);
        return ResponseEntity.ok(balance);
    }
//  depositing amount like http://localhost:8080/api/account/9a8f2710-14a6-4d2b-bdde-6f4b55941997/deposit?amount=100000
    @PostMapping("/{accountNr}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable UUID accountNr, @RequestParam double amount) {
        Account account = accountService.deposit(accountNr, amount);
        return ResponseEntity.ok(account);
    }
//  withdrawing some amount like http://localhost:8080/api/account/9a8f2710-14a6-4d2b-bdde-6f4b55941997/withdraw?amount=800
    @PostMapping("/{accountNr}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable UUID accountNr, @RequestParam double amount) {
        Account account = accountService.withdraw(accountNr, amount);
        return ResponseEntity.ok(account);
    }

}
