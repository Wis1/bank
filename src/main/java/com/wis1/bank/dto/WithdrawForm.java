package com.wis1.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WithdrawForm {
    private String accountNumber;
    private Long clientId;
    private BigDecimal sum;
}
