package org.example.tiltserver.exchange.service

import org.example.tiltserver.exchange.domain.ExchangeRate
import org.example.tiltserver.exchange.port.out.ExchangeRatePort
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class GetExchangeRateService(
    private val exchangeRatePort: ExchangeRatePort
) {

    fun getExchangeRate(currencyCode: String): ExchangeRate {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val today = LocalDate.now()

        // 1️⃣ 오늘
        val todayRates = safeGet(today.format(formatter))
        todayRates.find { it.currencyCode == currencyCode }?.let { return it }

        // 2️⃣ 전날
        val yesterdayRates = safeGet(today.minusDays(1).format(formatter))
        yesterdayRates.find { it.currencyCode == currencyCode }?.let { return it }

        // 3️⃣ 최후의 보루 (절대 안 터지게)
        return fallbackExchangeRate(currencyCode)
    }

    private fun safeGet(date: String): List<ExchangeRate> =
        try {
            exchangeRatePort.getExchangeRatesByDate(date)
        } catch (e: Exception) {
            emptyList()
        }

    private fun fallbackExchangeRate(currencyCode: String): ExchangeRate {
        return ExchangeRate(
            currencyCode = currencyCode,
            currencyName = "Fallback Currency",
            basePrice = BigDecimal("1300.00"),
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        )
    }
}
