package com.wis1.bank.controller.dto;


import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record TransactionLogDto(UUID id, Date timestamp, String type, BigDecimal amount, ClientDtoToAccount sender, ClientDtoToAccount receiver, boolean success) {
}
