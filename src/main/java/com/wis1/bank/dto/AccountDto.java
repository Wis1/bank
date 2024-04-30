package com.wis1.bank.dto;

import java.math.BigDecimal;

public record AccountDto(String accountNumber, BigDecimal balance) {
}
