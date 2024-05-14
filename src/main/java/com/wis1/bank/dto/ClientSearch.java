package com.wis1.bank.dto;

import com.wis1.bank.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClientSearch {

    private String name;
    private String lastname;
    private String pesel;
    private short age;
    private String phoneNumber;
    private Address address;
}
