package org.example.tilt_server.exchange.facade

import org.example.tilt_server.exchange.service.ExchangeNewsService
import org.example.tilt_server.exchange.service.GetExchangeRateService
import org.springframework.stereotype.Component

@Component
class ExchangeFacade(
    private val getExchangeRateService: GetExchangeRateService,
    private val exchangeNewsService: ExchangeNewsService
) {
    fun getExchangeRateInfo(currencyCode: String): Map<String, Any> {
        val result = getExchangeRateService.getExchangeRate(currencyCode)
        val news = exchangeNewsService.getExchangeRateNews(currencyCode)

        return mapOf(
            "data" to result.exchangeRate,
            "analysis" to result.analysis,
            "news" to news
        )
    }
}
