package com.wis1.bank.controller.dto;

import java.util.List;
import java.util.UUID;

public record ClientDto(UUID id, String name, String lastname, String pesel, short age, String phoneNumber, String email,
                        AddressDto address, List<AccountDto> accounts) {
}
