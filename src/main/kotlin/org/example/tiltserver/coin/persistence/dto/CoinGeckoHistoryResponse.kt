package org.example.tiltserver.coin.persistence.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CoinGeckoHistoryResponse(
    val id: String?,
    val symbol: String?,
    val name: String?,
    @JsonProperty("market_data")
    val marketData: MarketData?
)

data class MarketData(
    @JsonProperty("current_price")
    val currentPrice: Map<String, Double>?,
    @JsonProperty("market_cap")
    val marketCap: Map<String, Double>?,
    @JsonProperty("total_volume")
    val totalVolume: Map<String, Double>?
)
