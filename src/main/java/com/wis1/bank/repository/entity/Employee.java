package com.wis1.bank.repository.entity;

import jakarta.persistence.*;
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
    @Column
    private Role role;

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
        this.role= Role.EMPLOYEE;
    }
}
