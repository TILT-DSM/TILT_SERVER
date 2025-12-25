package org.example.tiltserver.coin.port.`in`

import org.example.tiltserver.coin.domain.CoinPriceWithAnalysis

interface GetCoinPriceUseCase {
    fun execute(coinSymbol: String): CoinPriceWithAnalysis
}
