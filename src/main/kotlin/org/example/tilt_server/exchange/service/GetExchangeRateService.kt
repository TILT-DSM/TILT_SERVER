package org.example.tilt_server.exchange.service

import org.example.tilt_server.exchange.domain.ExchangeRate
import org.example.tilt_server.exchange.domain.ExchangeRateWithAnalysis
import org.example.tilt_server.exchange.port.`in`.GetExchangeRateUseCase
import org.example.tilt_server.exchange.port.out.ExchangeRatePort
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class GetExchangeRateService(
    private val exchangeRatePort: ExchangeRatePort,
    private val exchangeAnalysisService: ExchangeAnalysisService
) : GetExchangeRateUseCase {

    override fun getExchangeRate(currencyCode: String): ExchangeRateWithAnalysis {
        val todayRates = exchangeRatePort.getLatestExchangeRates()
        val today = todayRates.find { it.currencyCode == currencyCode }
            ?: throw IllegalStateException("$currencyCode 에 대한 환율 정보를 찾을 수 없습니다.")

        val weekPrices = (1..7).mapNotNull { i ->
            val date = LocalDate.now().minusDays(i.toLong()).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            try {
                exchangeRatePort.getExchangeRatesByDate(date)
                    .find { it.currencyCode == currencyCode }?.basePrice
            } catch (_: Exception) {
                null
            }
        }

        val weekAvg = if (weekPrices.isNotEmpty()) {
            weekPrices.reduce { acc, bigDecimal -> acc + bigDecimal } / weekPrices.size.toBigDecimal()
        } else {
            today.basePrice
        }

        val diff = ((today.basePrice - weekAvg) / weekAvg * 100.toBigDecimal()).toDouble()

        val trend = when {
            diff > 0.5 -> "상승세"
            diff < -0.5 -> "하락세"
            else -> "안정세"
        }

        val analysis = exchangeAnalysisService.analyze(today.basePrice, trend, diff)

        return ExchangeRateWithAnalysis(today, analysis)
    }
}
