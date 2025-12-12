package org.example.tilt_server.coin.port.`in`

import org.example.tilt_server.coin.domain.CoinPriceWithAnalysis

interface GetCoinPriceUseCase {
    fun execute(coinSymbol: String): CoinPriceWithAnalysis
}
