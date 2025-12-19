package org.example.tilt_server.exchange.domain

data class ExchangeRateWithAnalysis(
    val exchangeRate: ExchangeRate,
    val analysis: String
)
