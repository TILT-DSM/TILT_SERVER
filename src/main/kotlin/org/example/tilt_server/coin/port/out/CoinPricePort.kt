package org.example.tilt_server.coin.port.out

import org.example.tilt_server.coin.domain.CoinInfo

interface CoinPricePort {
    fun getCoinPrice(coinSymbol: String): CoinInfo
    fun getCoinPriceByDate(coinSymbol: String, date: String): CoinInfo?
}
