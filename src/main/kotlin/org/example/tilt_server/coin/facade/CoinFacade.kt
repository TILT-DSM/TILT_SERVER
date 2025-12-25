package org.example.tilt_server.coin.facade

import org.example.tilt_server.coin.service.CoinNewsService
import org.example.tilt_server.coin.service.GetCoinPriceService
import org.springframework.stereotype.Component

@Component
class CoinFacade(
    private val getCoinPriceService: GetCoinPriceService,
    private val coinNewsService: CoinNewsService,
) {
    fun getCoinInfo(coinSymbol: String): Map<String, Any> {
        val result = getCoinPriceService.execute(coinSymbol)
        val news = coinNewsService.getCoinNews(coinSymbol) // Need to add this method to GoogleNewsService

        return mapOf(
            "data" to result.coinInfo,
            "analysis" to result.analysis,
            "news" to news
        )
    }
}
