package com.wis1.bank.repository;

import com.wis1.bank.repository.entity.Account;
import com.wis1.bank.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    void deleteByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByClientAndAccountNumber(Client client, String accountNumber);
}
