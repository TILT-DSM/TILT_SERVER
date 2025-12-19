package org.example.tilt_server.exchange.port.out

import org.example.tilt_server.exchange.domain.ExchangeRate

interface ExchangeRatePort {
    fun getLatestExchangeRates(): List<ExchangeRate>
    fun getExchangeRatesByDate(date: String): List<ExchangeRate>
}
