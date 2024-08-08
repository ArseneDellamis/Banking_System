package com.BankingSystem.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_user")
public class AccountUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_user_id")
    private long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "accountUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;
}
