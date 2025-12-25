package org.example.tiltserver.exchange.port.out

import org.example.tiltserver.exchange.domain.ExchangeRate

interface ExchangeRatePort {
    fun getLatestExchangeRates(): List<ExchangeRate>
    fun getExchangeRatesByDate(date: String): List<ExchangeRate>
}
