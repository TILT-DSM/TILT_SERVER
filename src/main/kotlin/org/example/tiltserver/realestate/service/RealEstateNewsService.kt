package org.example.tiltserver.realestate.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RealEstateNewsService(
    @Value("\${newsapi.key}") private val apiKey: String
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val client = WebClient.builder()
        .baseUrl("https://newsapi.org/v2")
        .defaultHeader("User-Agent", "Mozilla/5.0")
        .build()

    fun getRealEstateNews(regionCode: String): List<Map<String, String>> {
        if (apiKey.isBlank()) return emptyList()

        return try {
            val query = when (regionCode.uppercase()) {
                "SEOUL" -> "Seoul housing market OR Korea real estate"
                "BUSAN" -> "Busan housing market OR Korea real estate"
                "DAEGU" -> "Korea real estate market"
                else -> "Korea real estate market"
            }

            val response = client.get()
                .uri {
                    it.path("/everything")
                        .queryParam("q", query)
                        .queryParam("sortBy", "publishedAt")
                        .queryParam("pageSize", 5)
                        .queryParam("apiKey", apiKey)
                        .build()
                }
                .retrieve()
                .bodyToMono(Map::class.java)
                .block() ?: return emptyList()

            val articles = response["articles"] as? List<Map<*, *>> ?: emptyList()

            articles.mapNotNull {
                val title = it["title"]?.toString()
                val url = it["url"]?.toString()
                if (title != null && url != null) {
                    mapOf("title" to title, "url" to url)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
