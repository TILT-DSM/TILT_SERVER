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

    // 🔒 마지막으로 성공한 금 가격 (fallback 용)
    @Volatile
    private var lastValidGoldPrice: Double = 4615.0

    override fun getGoldPrice(): GoldPrice {
        return try {
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
                .block()

            if (rawJson.isNullOrBlank()) {
                log.warn("METALPRICE RAW RESPONSE EMPTY → fallback")
                return fallbackPrice()
            }

            log.info("METALPRICE RAW RESPONSE = {}", rawJson)

            val mapper = jacksonObjectMapper()
            val response = mapper.readValue(rawJson, GoldApiRawResponse::class.java)

            response.toEntityWithFallback()

        } catch (e: Exception) {
            log.warn("METALPRICE ERROR → fallback", e)
            fallbackPrice()
        }
    }

    private fun GoldApiRawResponse.toEntityWithFallback(): GoldPrice {
        if (success == false) {
            log.warn("METALPRICE API returned success=false; using fallback")
            return fallbackPrice()
        }

        val xauRate = rates?.XAU

        val usdPerXau = if (xauRate == null || !xauRate.isFinite() || xauRate <= 0.0) {
            log.warn("XAU rate missing or invalid → fallback")
            lastValidGoldPrice
        } else {
            val price = 1.0 / xauRate
            lastValidGoldPrice = price
            price
        }

        return GoldPrice(
            price = usdPerXau,
            currency = "USD",
            timestamp = timestamp ?: (System.currentTimeMillis() / 1000)
        )
    }

    private fun fallbackPrice(): GoldPrice =
        GoldPrice(
            price = lastValidGoldPrice,
            currency = "USD",
            timestamp = System.currentTimeMillis() / 1000
        )
}
