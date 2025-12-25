package org.example.tilt_server.gold.persistence.adapter

import org.example.tilt_server.gold.entity.GoldPrice
import org.example.tilt_server.gold.persistence.dto.GoldApiRawResponse
import org.example.tilt_server.gold.port.out.GoldPricePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GoldApiAdapter(
    @Value("\${goldapi.key}") private val apiKey: String
) : GoldPricePort {

    private val client = WebClient.builder()
        .baseUrl("https://www.goldapi.io/api")
        .defaultHeader("x-access-token", apiKey)
        .defaultHeader("Content-Type", "application/json")
        .build()

    override fun getGoldPrice(): GoldPrice {
        val response = client.get()
            .uri("/XAU/USD")
            .retrieve()
            .bodyToMono(GoldApiRawResponse::class.java)
            .block() ?: throw RuntimeException("금 시세 조회 실패")

        return response.toEntity()
    }

    fun getGoldPriceByDate(date: String): GoldPrice {
        val response = client.get()
            .uri("/XAU/USD/$date")
            .retrieve()
            .bodyToMono(GoldApiRawResponse::class.java)
            .block() ?: throw RuntimeException("금 시세 조회 실패")

        return response.toEntity()
    }

    private fun GoldApiRawResponse.toEntity(): GoldPrice =
        GoldPrice(
            price = price ?: 0.0,
            currency = currency ?: "USD",
            timestamp = timestamp ?: 0L,
            open = open,
            high = high,
            low = low
        )
}
