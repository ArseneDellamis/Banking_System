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
import java.util.Optional;
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
public ResponseEntity<Response> creatingAccount(@RequestBody AccountRegister register) {
    // Check if the user already exists based on email (or other unique identifier)
    Optional<AccountUser> existingUserOpt = accountUserRepo.findByEmail(register.getEmail());

    AccountUser user;
    if (existingUserOpt.isPresent()) {
        // User exists, use the existing user
        user = existingUserOpt.get();
    } else {
        // User doesn't exist, create a new one
        user = new AccountUser();
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setEmail(register.getEmail());
        user = accountUserRepo.save(user);
    }
    // Validate and set the account type
    String acType = register.getAccountType().name().toUpperCase();
    AccountType accountType;
    try {
        accountType = AccountType.valueOf(acType);
    } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Invalid account type: " + acType);
    }

    // Create a new account for the user
    Account newAccount = Account.builder()
            .accountType(accountType)
            .accountUser(user)
            .password(register.getPassword()) // Ensure you are securely handling passwords
            .balance(0.0) // Set an initial balance if required
            .build();

    Account savedAccount = accountRepo.save(newAccount);

    // Prepare the response
    Response<Account> accountResponse = new Response<>();
    accountResponse.setStatus(HttpStatus.CREATED);
    accountResponse.setMessage("Account created successfully");
//    accountResponse.setData(savedAccount);

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
