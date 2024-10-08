package com.wis1.bank.controller.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClientForm {

    private String name;
    private String lastname;
    private String login;
    private String pesel;
    private short age;
    private String phoneNumber;
    private String email;
    private String password;
    private AddressForm address;

}
