package com.wis1.bank.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @Column(unique = true)
    private UUID id = UUID.randomUUID();
    @Column
    private String accountNumber;
    @ManyToOne
    private Client client;
    @Column
    private BigDecimal balance;
    @OneToMany
    private List<TransactionLog> history;

    public Account(String accountNumber, Client client, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.client = client;
        this.balance = balance;
    }

    public Account(String accountNumber, Client client) {
        this.accountNumber= accountNumber;
        this.client=client;
        this.balance=BigDecimal.ZERO;
        this.history= new ArrayList<>();
    }

    public void addTransaction(TransactionLog transactionLog) {
        history.add(transactionLog);
    }
}


