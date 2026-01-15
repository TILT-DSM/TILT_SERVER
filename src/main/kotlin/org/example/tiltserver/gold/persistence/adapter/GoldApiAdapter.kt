package org.example.tiltserver.gold.persistence.adapter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.example.tiltserver.gold.entity.GoldPrice
import org.example.tiltserver.gold.persistence.dto.GoldApiRawResponse
import org.example.tiltserver.gold.port.out.GoldPricePort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GoldApiAdapter(
    @Value("\${GOLD_API_KEY}") private val apiKey: String
) : GoldPricePort {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val client = WebClient.builder()
        .baseUrl("https://api.metalpriceapi.com")
        .build()

    override fun getGoldPrice(): GoldPrice {
        // 🔥 1) RAW JSON 먼저 받기
        val rawJson = client.get()
            .uri {
                it.path("/v1/latest")
                    .queryParam("api_key", apiKey)
                    .queryParam("base", "USD")
                    .queryParam("currencies", "XAU")
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .block() ?: throw IllegalStateException("MetalPrice API returned null")

        log.info("METALPRICE RAW RESPONSE = {}", rawJson)

        // 🔥 2) JSON → DTO 수동 변환
        val mapper = jacksonObjectMapper()
        val response = mapper.readValue(rawJson, GoldApiRawResponse::class.java)

        // 🔥 3) API 실패 응답 차단
        require(response.success != false) {
            "MetalPrice API error response: $rawJson"
        }

        return response.toEntity()
    }

    private fun GoldApiRawResponse.toEntity(): GoldPrice {
        val xauRate = rates?.XAU
            ?: throw IllegalStateException("XAU rate missing in response")

        require(xauRate.isFinite() && xauRate > 0.0) {
            "Invalid XAU rate: $xauRate"
        }

        val usdPerXau = 1.0 / xauRate

        require(usdPerXau.isFinite() && usdPerXau > 0.0) {
            "Invalid USD/XAU price computed: $usdPerXau (rate=$xauRate)"
        }

        return GoldPrice(
            price = usdPerXau,
            currency = "USD",
            timestamp = timestamp ?: 0L
        )
    }
}
