package com.wis1.bank.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WithdrawDepositForm {

    private String accountNumber;
    private Long clientId;
    private BigDecimal sum;
}
