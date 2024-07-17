package com.wis1.bank.controller.dto;

import java.util.List;

public record RateDto(
        String table,
        String number,
        String effectiveDate,
        List<CurrencyRate> rates
) {
    public record CurrencyRate(
            String currency,
            String code,
            double mid
    ) {}
}
