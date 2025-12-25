package org.example.tiltserver.exchange.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ExchangeNewsService(
    @Value("\${newsapi.key}") private val apiKey: String
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val client = WebClient.builder()
        .baseUrl("https://newsapi.org/v2")
        .defaultHeader("User-Agent", "Mozilla/5.0")
        .build()

    fun getExchangeRateNews(currencyCode: String): List<Map<String, String>> {
        return try {
            val response = client.get()
                .uri {
                    it.path("/everything")
                        .queryParam("q", "$currencyCode exchange rate")
                        .queryParam("language", "en")
                        .queryParam("sortBy", "publishedAt")
                        .queryParam("pageSize", 5)
                        .queryParam("apiKey", apiKey)
                        .build()
                }
                .retrieve()
                .bodyToMono(Map::class.java)
                .block() ?: return emptyList()

            val articles = response["articles"] as? List<Map<*, *>> ?: emptyList()

            log.info("NEWS RAW = {}", articles)

            articles.map {
                mapOf(
                    "title" to it["title"].toString(),
                    "url" to it["url"].toString()
                )
            }
        } catch (e: Exception) {
            log.error("NEWS API ERROR", e)
            emptyList()
        }
    }
}
