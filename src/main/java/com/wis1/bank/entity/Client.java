package com.wis1.bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;
    @Column
    private String lastname;
    @Column
    private String pesel;

    @OneToMany(mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Account> accounts;

    public Client(String name, String lastname, String pesel) {
        this.name = name;
        this.lastname = lastname;
        this.pesel = pesel;
        this.accounts = new ArrayList<>();
    }
}
