package com.wis1.bank.dto;

import java.util.List;

public record ClientDto(Long id, String name, String lastname, String pesel, List<AccountDto> accounts) {
}
