package com.BankingSystem.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByAccountUserId(Long accountUserId);
//    List<Account> findByAccountUserName(String name);
}
