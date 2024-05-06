package com.wis1.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransferForm {

    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
}
