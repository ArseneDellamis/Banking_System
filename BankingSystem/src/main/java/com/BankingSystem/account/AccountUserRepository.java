package com.BankingSystem.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

    Optional<AccountUser> findByEmail(String email);
}
