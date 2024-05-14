package com.wis1.bank.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressForm {

    private String city;
    private String street;
    private short buildingNumber;
}
