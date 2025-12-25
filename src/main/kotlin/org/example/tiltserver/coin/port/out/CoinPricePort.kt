package org.example.tiltserver.coin.port.out

import org.example.tiltserver.coin.domain.CoinInfo

interface CoinPricePort {
    fun getCoinPrice(coinSymbol: String): CoinInfo
    fun getCoinPriceByDate(coinSymbol: String, date: String): CoinInfo?
}
