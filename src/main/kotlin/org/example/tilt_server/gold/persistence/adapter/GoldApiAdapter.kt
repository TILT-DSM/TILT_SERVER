package org.example.tilt_server.gold.persistence.adapter

import org.example.tilt_server.gold.entity.GoldPrice
import org.example.tilt_server.gold.persistence.dto.GoldApiResponse
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
            .bodyToMono(GoldApiResponse::class.java)
            .block() ?: throw RuntimeException("금 시세 조회 실패")

        return GoldPrice(
            price = response.price ?: 0.0,
            currency = response.currency ?: "",
            timestamp = response.timestamp ?: 0L,
            open = response.open ?: 0.0,
            high = response.high ?: 0.0,
            low = response.low ?: 0.0
        )
    }
}