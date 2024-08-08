package com.BankingSystem.payLoad;

import com.BankingSystem.account.AccountType;
import com.BankingSystem.responseHandler.AccountTypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegister {
    private String firstName;
    private String lastName;
    private String email;

    @JsonDeserialize(using = AccountTypeDeserializer.class)
    private AccountType accountType;
    private String password;
}
