package com.wis1.bank.controller.dto;

import java.util.UUID;

public record ClientDtoToAccount(UUID id, String name, String lastname, String pesel) {
}
