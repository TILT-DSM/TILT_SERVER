package org.example.tilt_server.exchange.domain

import java.math.BigDecimal

data class ExchangeRate(
    val currencyCode: String, // 통화코드 (e.g., USD)
    val currencyName: String, // 통화명 (e.g., 미국 달러)
    val basePrice: BigDecimal, // 매매 기준율
    val date: String // 조회일자
)
