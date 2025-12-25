package org.example.tiltserver.exchange.facade

import org.example.tiltserver.exchange.service.ExchangeAnalysisService
import org.example.tiltserver.exchange.service.ExchangeNewsService
import org.example.tiltserver.exchange.service.GetExchangeRateService
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ExchangeFacade(
    private val getExchangeRateService: GetExchangeRateService,
    private val exchangeNewsService: ExchangeNewsService,
    private val exchangeAnalysisService: ExchangeAnalysisService
) {
    fun getExchangeRateInfo(currencyCode: String): Map<String, Any> {
        val rate = getExchangeRateService.getExchangeRate(currencyCode)

        // 임시 기준값 (나중에 평균으로 바꿔도 됨)
        val base = BigDecimal("1250")
        val diff = rate.basePrice.subtract(base)
            .divide(base, 4, java.math.RoundingMode.HALF_UP)
            .multiply(BigDecimal(100))
            .toDouble()

        val trend = when {
            diff > 1 -> "상승세"
            diff < -1 -> "하락세"
            else -> "보합"
        }

        val analysis = exchangeAnalysisService.analyze(
            rate.basePrice,
            trend,
            kotlin.math.abs(diff)
        )

        val news = exchangeNewsService.getExchangeRateNews(currencyCode)

        return mapOf(
            "exchangeRate" to rate,
            "analysis" to analysis,
            "news" to news
        )
    }
}
