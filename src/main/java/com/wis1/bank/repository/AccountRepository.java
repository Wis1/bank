package com.wis1.bank.repository;

import com.wis1.bank.entity.Account;
import com.wis1.bank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    void deleteByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByClientAndAccountNumber(Client client, String accountNumber);
}
