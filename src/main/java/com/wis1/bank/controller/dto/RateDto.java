package com.wis1.bank.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RateDto(
        @JsonProperty("table") String table,
        @JsonProperty("no") String number,
        @JsonProperty("effectiveDate") String effectiveDate,
        @JsonProperty("rates") List<CurrencyRate> rates
) {
    public record CurrencyRate(
            @JsonProperty("currency") String currency,
            @JsonProperty("code") String code,
            @JsonProperty("mid") double mid
    ) {}
}
