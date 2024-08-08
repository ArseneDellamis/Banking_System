package com.BankingSystem.responseHandler;

import com.BankingSystem.account.AccountType;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class AccountTypeDeserializer
        extends JsonDeserializer<AccountType> {
    @Override
    public AccountType deserialize(JsonParser p,
                                   DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = p.getText().toUpperCase();
        try {
            return AccountType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid account type: " + value);
        }
    }
}
