package org.example.tilt_server.coin.persistence.adapter

import org.example.tilt_server.coin.domain.CoinInfo
import org.example.tilt_server.coin.persistence.dto.CoinGeckoHistoryResponse
import org.example.tilt_server.coin.persistence.dto.CoinGeckoSimplePriceResponse
import org.example.tilt_server.coin.persistence.dto.CoinGeckoSimplePriceWrapper
import org.example.tilt_server.coin.port.out.CoinPricePort
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class CoinApiAdapter : CoinPricePort {

    private val client = WebClient.builder()
        .baseUrl("https://api.coingecko.com/api/v3")
        .build()

    // A simple mapping from common coin symbols to CoinGecko IDs
    private val coinIdMap = mapOf(
        "BTC" to "bitcoin",
        "ETH" to "ethereum",
        "XRP" to "ripple",
        "DOGE" to "dogecoin",
        "LTC" to "litecoin",
        "ADA" to "cardano",
        "SOL" to "solana",
        "DOT" to "polkadot",
        "TRX" to "tron",
        "BNB" to "binancecoin",
        "USDC" to "usd-coin",
        "USDT" to "tether",
        "stETH" to "staked-ether",
        "AVAX" to "avalanche-2",
        "SHIB" to "shiba-inu",
        "WBTC" to "wrapped-bitcoin",
        "DAI" to "dai",
        "LINK" to "chainlink",
        "LEO" to "leo-token",
        "UNI" to "uniswap",
        "OKB" to "okb",
        "ETC" to "ethereum-classic",
        "APT" to "aptos",
        "XLM" to "stellar"
    )

    private fun getCoinGeckoId(coinSymbol: String): String {
        return coinIdMap[coinSymbol.uppercase()] ?: throw IllegalArgumentException("Unsupported coin symbol: $coinSymbol")
    }

    override fun getCoinPrice(coinSymbol: String): CoinInfo {
        val coinId = getCoinGeckoId(coinSymbol)
        val responseMap = client.get()
            .uri("/simple/price?ids=$coinId&vs_currencies=usd&include_market_cap=true&include_24hr_vol=true&include_24hr_change=true")
            .retrieve()
            .bodyToMono(Map::class.java) // Read as a Map because the root key is dynamic
            .block() as Map<String, Map<String, Any>>? ?: throw RuntimeException("코인 시세 조회 실패: $coinSymbol")

        val coinData = responseMap?.get(coinId) ?: throw RuntimeException("코인 데이터 없음: $coinSymbol")

        val price = (coinData["usd"] as? Number)?.toDouble() ?: 0.0
        val marketCap = (coinData["usd_market_cap"] as? Number)?.toDouble()
        val volume = (coinData["usd_24h_vol"] as? Number)?.toDouble()
        val change24h = (coinData["usd_24h_change"] as? Number)?.toDouble()

        // CoinGecko simple price doesn't directly provide open, high, low, or a specific timestamp for a single point.
        // We can approximate or use a different endpoint if more precise historical OHLCV data is needed.
        // For simplicity, using current price as open, high, low if not available.
        val currentTimestamp = Instant.now().epochSecond

        return CoinInfo(
            symbol = coinSymbol.uppercase(),
            price = price,
            currency = "USD",
            timestamp = currentTimestamp,
            open = price, // Approximation
            high = price, // Approximation
            low = price,  // Approximation
            volume = volume,
            marketCap = marketCap
        )
    }

    override fun getCoinPriceByDate(coinSymbol: String, date: String): CoinInfo? {
        val coinId = getCoinGeckoId(coinSymbol)
        // CoinGecko history API uses date format dd-mm-yyyy
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = LocalDate.parse(date).format(formatter)

        val response = client.get()
            .uri("/coins/$coinId/history?date=$formattedDate&localization=false")
            .retrieve()
            .bodyToMono(CoinGeckoHistoryResponse::class.java)
            .block() ?: return null

        val usdPrice = response.marketData?.currentPrice?.get("usd")

        return if (usdPrice != null) {
            val historicalTimestamp = LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toEpochSecond()
            CoinInfo(
                symbol = coinSymbol.uppercase(),
                price = usdPrice,
                currency = "USD",
                timestamp = historicalTimestamp,
                open = usdPrice, // Approximation
                high = usdPrice, // Approximation
                low = usdPrice,  // Approximation
                volume = response.marketData.totalVolume?.get("usd"),
                marketCap = response.marketData.marketCap?.get("usd")
            )
        } else {
            null
        }
    }
}
