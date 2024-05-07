package com.wis1.bank.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClientForm {

    private String name;
    private String lastname;
    private String pesel;
}
