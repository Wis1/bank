package com.wis1.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @Column(unique = true)
    private UUID id=UUID.randomUUID();
    @Column
    private String username;
    @Column
    private String password;

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
