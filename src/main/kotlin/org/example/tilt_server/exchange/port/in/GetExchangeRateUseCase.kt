package org.example.tilt_server.exchange.port.`in`

import org.example.tilt_server.exchange.domain.ExchangeRateWithAnalysis

interface GetExchangeRateUseCase {
    fun getExchangeRate(currencyCode: String): ExchangeRateWithAnalysis
}
