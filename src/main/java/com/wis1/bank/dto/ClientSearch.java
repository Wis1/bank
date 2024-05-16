package com.wis1.bank.dto;

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
