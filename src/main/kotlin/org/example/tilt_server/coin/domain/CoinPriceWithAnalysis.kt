package org.example.tilt_server.coin.domain

data class CoinPriceWithAnalysis(
    val coinInfo: CoinInfo,
    val analysis: String
)
