package org.example.tiltserver.exchange.port.`in`

import org.example.tiltserver.exchange.domain.ExchangeRateWithAnalysis

interface GetExchangeRateUseCase {
    fun getExchangeRate(currencyCode: String): ExchangeRateWithAnalysis
}
