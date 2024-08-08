package com.BankingSystem.controller;

import com.BankingSystem.account.*;
import com.BankingSystem.payLoad.AccountRegister;
import com.BankingSystem.responseHandler.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepo;
    private final AccountUserRepository accountUserRepo;

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
}
