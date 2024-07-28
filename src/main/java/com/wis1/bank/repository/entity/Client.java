package com.wis1.bank.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Getter
@Setter
public class Client {

    @Id
    @Column(unique = true)
    private UUID id=UUID.randomUUID();
    @Column
    private String name;
    @Column
    private String lastname;
    @Column
    private String pesel;
    @Column
    private short age;
    @Column
    private String phoneNumber;
    @Column
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

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

    public Client(String name, String lastname, String pesel, short age, String phoneNumber, String email, Address address) {
        this.name = name;
        this.lastname = lastname;
        this.pesel = pesel;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }
}
