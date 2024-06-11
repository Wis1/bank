package com.wis1.bank.controller.dto;

import java.math.BigDecimal;

public record AccountDto(String accountNumber, BigDecimal balance) {
}
