package org.example.tilt_server.exchange.persistence.adapter

import org.example.tilt_server.exchange.domain.ExchangeRate
import org.example.tilt_server.exchange.persistence.dto.ExchangeApiResponse
import org.example.tilt_server.exchange.port.out.ExchangeRatePort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal

@Component
class ExchangeApiAdapter(
    @Qualifier("defaultWebClient")
    private val webClient: WebClient,
    @Value("\${exchangeapi.key}") private val apiKey: String,
    @Value("\${exchangeapi.base-url}") private val baseUrl: String
) : ExchangeRatePort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getExchangeRatesByDate(date: String): List<ExchangeRate> {
        return try {
            val response = webClient.get()
                .uri(baseUrl) {
                    it.path("site/program/financial/exchangeJSON")
                        .queryParam("authkey", apiKey)
                        .queryParam("searchdate", date)
                        .queryParam("data", "AP01")
                        .build()
                }
                .retrieve()
                .bodyToMono(object :
                    ParameterizedTypeReference<List<ExchangeApiResponse>>() {})
                .block() ?: emptyList()

            log.info("EXCHANGE RAW ({}) = {}", date, response)

            response.map {
                ExchangeRate(
                    currencyCode = it.currencyUnit,
                    currencyName = it.currencyName,
                    basePrice = BigDecimal(it.dealingBaseRate.replace(",", "")),
                    date = date
                )
            }
        } catch (e: Exception) {
            log.error("EXCHANGE API ERROR", e)
            emptyList()
        }
    }
    override fun getLatestExchangeRates(): List<ExchangeRate> {
        return getExchangeRatesByDate(
            java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))
        )
    }

}
