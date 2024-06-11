package com.wis1.bank.controller.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WithdrawDepositForm {

    private String accountNumber;
    private UUID clientId;
    private BigDecimal sum;
}
