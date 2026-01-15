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

    private fun GoldApiRawResponse.toEntity(): GoldPrice =
        GoldPrice(
            price = convertRateToUsdPerXau(rates?.XAU ?: 0.0),
            currency = "XAU",
            timestamp = timestamp ?: 0L
        )

    private fun convertRateToUsdPerXau(rate: Double): Double {
        if (rate == 0.0) return 0.0
        return 1.0 / rate
    }
}
