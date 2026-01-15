package org.example.tiltserver.gold.persistence.adapter

import org.example.tiltserver.gold.entity.GoldPrice
import org.example.tiltserver.gold.persistence.dto.GoldApiRawResponse
import org.example.tiltserver.gold.port.out.GoldPricePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GoldApiAdapter(
    @Value("\${GOLD_API_KEY}") private val apiKey: String
) : GoldPricePort {

    private val client = WebClient.builder()
        .baseUrl("https://api.metalpriceapi.com")
        .build()

    override fun getGoldPrice(): GoldPrice {
        val response = client.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/v1/latest")
                    .queryParam("api_key", apiKey)
                    .queryParam("base", "USD")
                    .queryParam("currencies", "XAU")
                    .build()
            }
            .retrieve()
            .bodyToMono(GoldApiRawResponse::class.java)
            .block() ?: throw RuntimeException("Failed to fetch gold price")

        return response.toEntity()
    }

    fun getGoldPriceByDate(date: String): GoldPrice {
        val response = client.get()
            .uri { uriBuilder ->
                uriBuilder
                    // ✅ 날짜 조회면 /v1/{date} 가 맞을 가능성이 큼 (latest 고정이면 date 의미 없음)
                    .path("/v1/${date.trim()}")
                    .queryParam("api_key", apiKey)
                    .queryParam("base", "USD")
                    .queryParam("currencies", "XAU")
                    .build()
            }
            .retrieve()
            .bodyToMono(GoldApiRawResponse::class.java)
            .block() ?: throw RuntimeException("Failed to fetch gold price")

        return response.toEntity()
    }

    private fun GoldApiRawResponse.toEntity(): GoldPrice {
        val xauRate = rates?.XAU ?: throw IllegalStateException("XAU rate missing")

        require(xauRate.isFinite() && xauRate > 0.0) {
            "Invalid XAU rate: $xauRate"
        }

        val usdPerXau = 1.0 / xauRate

        require(usdPerXau.isFinite() && usdPerXau > 0.0) {
            "Invalid USD/XAU price computed: $usdPerXau (rate=$xauRate)"
        }

        return GoldPrice(
            price = usdPerXau,
            currency = "USD",          // ✅ 가격이 USD per XAU라서 USD가 맞음
            timestamp = timestamp ?: 0L
        )
    }
}
