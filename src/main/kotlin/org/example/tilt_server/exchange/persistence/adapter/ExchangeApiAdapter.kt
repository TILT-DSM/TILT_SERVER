package org.example.tilt_server.exchange.persistence.adapter

import org.example.tilt_server.exchange.domain.ExchangeRate
import org.example.tilt_server.exchange.persistence.dto.ExchangeApiResponse
import org.example.tilt_server.exchange.port.out.ExchangeRatePort
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class ExchangeApiAdapter(
    @Qualifier("defaultWebClient")
    private val webClient: WebClient,
    @Value("\${exchangeapi.key}") private val apiKey: String,
    @Value("\${exchangeapi.base-url}") private val baseUrl: String
) : ExchangeRatePort {

    override fun getLatestExchangeRates(): List<ExchangeRate> {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return getExchangeRatesByDate(today)
    }

    override fun getExchangeRatesByDate(date: String): List<ExchangeRate> {
        val response = webClient.get()
            .uri(baseUrl) { uriBuilder ->
                uriBuilder
                    .path("site/program/financial/exchangeJSON")
                    .queryParam("authkey", apiKey)
                    .queryParam("searchdate", date)
                    .queryParam("data", "AP01")
                    .build()
            }
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<ExchangeApiResponse>>() {})
            .block() ?: emptyList()

        return response.map {
            ExchangeRate(
                currencyCode = it.currencyUnit,
                currencyName = it.currencyName,
                basePrice = BigDecimal(it.dealingBaseRate.replace(",", "")),
                date = date
            )
        }
    }
}
