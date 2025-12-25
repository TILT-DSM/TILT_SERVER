package org.example.tiltserver.coin.domain

data class CoinPriceWithAnalysis(
    val coinInfo: CoinInfo,
    val analysis: String
)
