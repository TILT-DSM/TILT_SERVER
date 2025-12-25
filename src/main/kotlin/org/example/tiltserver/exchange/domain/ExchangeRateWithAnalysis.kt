package org.example.tiltserver.exchange.domain

data class ExchangeRateWithAnalysis(
    val exchangeRate: ExchangeRate,
    val analysis: String
)
