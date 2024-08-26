package com.wis1.bank.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientSearch {

    private String name;
    private String lastname;
    private String login;
    private String pesel;
    private Short age;
    private String phoneNumber;
    private AddressSearch address;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class AddressSearch {
        private String city;
        private String street;
        private Short buildingNumber;
    }
}
