package org.example.tilt_server.coin.persistence.dto

import com.fasterxml.jackson.annotation.JsonProperty

// For CoinGecko simple price endpoint: /simple/price
data class CoinGeckoSimplePriceResponse(
    val id: String, // coin ID like "bitcoin"
    val usd: Double?,
    @JsonProperty("usd_market_cap")
    val usdMarketCap: Double?,
    @JsonProperty("usd_24h_vol")
    val usd24hVol: Double?,
    @JsonProperty("usd_24h_change")
    val usd24hChange: Double?
)

// For CoinGecko history endpoint: /coins/{id}/history
data class CoinGeckoHistoryResponse(
    val id: String,
    val symbol: String,
    val name: String,
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

// Wrapper for the simple price response because the key is dynamic (e.g., "bitcoin")
data class CoinGeckoSimplePriceWrapper(
    @JsonProperty("bitcoin") val bitcoin: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("ethereum") val ethereum: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("ripple") val ripple: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("dogecoin") val dogecoin: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("litecoin") val litecoin: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("cardano") val cardano: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("solana") val solana: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("polkadot") val polkadot: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("tron") val tron: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("binancecoin") val binancecoin: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("usd-coin") val usdCoin: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("tether") val tether: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("staked-ether") val stakedEther: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("avalanche-2") val avalanche: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("shiba-inu") val shibaInu: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("wrapped-bitcoin") val wrappedBitcoin: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("dai") val dai: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("chainlink") val chainlink: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("leo-token") val leoToken: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("uniswap") val uniswap: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("okb") val okb: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("monero") val monero: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("ethereum-classic") val ethereumClassic: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("aptos") val aptos: CoinGeckoSimplePriceResponse? = null,
    @JsonProperty("stellar") val stellar: CoinGeckoSimplePriceResponse? = null
) {
    fun getPriceResponse(coinId: String): CoinGeckoSimplePriceResponse? {
        return when(coinId) {
            "bitcoin" -> bitcoin
            "ethereum" -> ethereum
            "ripple" -> ripple
            "dogecoin" -> dogecoin
            "litecoin" -> litecoin
            "cardano" -> cardano
            "solana" -> solana
            "polkadot" -> polkadot
            "tron" -> tron
            "binancecoin" -> binancecoin
            "usd-coin" -> usdCoin
            "tether" -> tether
            "staked-ether" -> stakedEther
            "avalanche-2" -> avalanche
            "shiba-inu" -> shibaInu
            "wrapped-bitcoin" -> wrappedBitcoin
            "dai" -> dai
            "chainlink" -> chainlink
            "leo-token" -> leoToken
            "uniswap" -> uniswap
            "okb" -> okb
            "monero" -> monero
            "ethereum-classic" -> ethereumClassic
            "aptos" -> aptos
            "stellar" -> stellar
            else -> null
        }
    }
}
