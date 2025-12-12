package org.example.tilt_server.coin.domain

data class CoinInfo(
    val symbol: String, // e.g., "BTC", "ETH"
    val price: Double,
    val currency: String, // e.g., "USD"
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val volume: Double? = null,
    val marketCap: Double? = null
)
