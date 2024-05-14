package com.wis1.bank.dto;


import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record TransactionDto(UUID id, Date timestamp, String type, BigDecimal amount, ClientDtoToAccount sender, ClientDtoToAccount receiver) {
}
