package org.example.tiltserver.coin.persistence.adapter

import org.example.tiltserver.coin.domain.CoinInfo
import org.example.tiltserver.coin.persistence.dto.CoinGeckoHistoryResponse
import org.example.tiltserver.coin.port.out.CoinPricePort
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

    // Coin symbol → CoinGecko ID
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
        "STETH" to "staked-ether", // ❗ 대문자로 수정
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

    private fun getCoinGeckoId(symbol: String): String =
        coinIdMap[symbol.uppercase()]
            ?: throw IllegalArgumentException("Unsupported coin symbol: $symbol")

    /**
     * 현재 코인 시세 조회
     * - simple/price API는 OHLC 제공 ❌
     * - timestamp는 조회 시점 기준
     */
    override fun getCoinPrice(coinSymbol: String): CoinInfo {
        val coinId = getCoinGeckoId(coinSymbol)

        val response = client.get()
            .uri(
                "/simple/price" +
                    "?ids=$coinId" +
                    "&vs_currencies=usd" +
                    "&include_market_cap=true" +
                    "&include_24hr_vol=true" +
                    "&include_24hr_change=true"
            )
            .retrieve()
            .bodyToMono(Map::class.java)
            .block() as? Map<String, Map<String, Any>>
            ?: throw RuntimeException("코인 시세 조회 실패: $coinSymbol")

        val coinData = response[coinId]
            ?: throw RuntimeException("코인 데이터 없음: $coinSymbol")

        val price = (coinData["usd"] as? Number)?.toDouble() ?: 0.0
        val marketCap = (coinData["usd_market_cap"] as? Number)?.toDouble()
        val volume = (coinData["usd_24h_vol"] as? Number)?.toDouble()

        return CoinInfo(
            symbol = coinSymbol.uppercase(),
            price = price,
            currency = "USD",
            timestamp = Instant.now().epochSecond, // 조회 시점 기준
            open = null,
            high = null,
            low = null,
            volume = volume,
            marketCap = marketCap
        )
    }

    /**
     * 특정 날짜 코인 시세 조회
     * - date 형식: yyyy-MM-dd
     */
    override fun getCoinPriceByDate(coinSymbol: String, date: String): CoinInfo? {
        val coinId = getCoinGeckoId(coinSymbol)

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate = LocalDate.parse(date).format(formatter)

        val response = client.get()
            .uri("/coins/$coinId/history?date=$formattedDate&localization=false")
            .retrieve()
            .bodyToMono(CoinGeckoHistoryResponse::class.java)
            .block() ?: return null

        val usdPrice = response.marketData?.currentPrice?.get("usd")
            ?: return null

        val timestamp = LocalDate.parse(date)
            .atStartOfDay(ZoneOffset.UTC)
            .toEpochSecond()

        return CoinInfo(
            symbol = coinSymbol.uppercase(),
            price = usdPrice,
            currency = "USD",
            timestamp = timestamp,
            open = null,
            high = null,
            low = null,
            volume = response.marketData.totalVolume?.get("usd"),
            marketCap = response.marketData.marketCap?.get("usd")
        )
    }
}
