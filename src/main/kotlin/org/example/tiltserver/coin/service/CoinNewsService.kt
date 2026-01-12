package org.example.tiltserver.coin.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class CoinNewsService(
    @Value("\${newsapi.key}") private val apiKey: String
) {

    private val client = WebClient.builder()
        .baseUrl("https://newsapi.org/v2")
        .defaultHeader("User-Agent", "Mozilla/5.0")
        .build()

    fun getCoinNews(symbol: String): List<Map<String, String>> {
        val keyword = when (symbol.uppercase()) {
            "BTC" -> "Bitcoin"
            "ETH" -> "Ethereum"
            "XRP" -> "Ripple"
            else -> symbol
        }

        return try {
            val response = client.get()
                .uri {
                    it.path("/everything")
                        .queryParam("q", keyword)
                        .queryParam("language", "en")
                        .queryParam("pageSize", 5)
                        .queryParam("apiKey", apiKey)
                        .build()
                }
                .retrieve()
                .bodyToMono(Map::class.java)
                .block() ?: return emptyList()

            val articles = response["articles"] as? List<Map<*, *>> ?: emptyList()

            articles.map {
                mapOf(
                    "title" to it["title"].toString(),
                    "url" to it["url"].toString()
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
