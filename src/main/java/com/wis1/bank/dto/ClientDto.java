package com.wis1.bank.dto;

import java.util.List;
import java.util.UUID;

public record ClientDto(UUID id, String name, String lastname, String pesel, List<AccountDto> accounts) {
}
