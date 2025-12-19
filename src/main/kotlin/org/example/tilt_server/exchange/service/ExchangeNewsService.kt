package org.example.tilt_server.exchange.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ExchangeNewsService {

    private val client = WebClient.create("https://news.google.com/rss/search")

    fun getExchangeRateNews(currencyCode: String): List<Map<String, String>> {
        return try {
            val query = "${currencyCode}+exchange+rate"
            val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
            val xml = client.get()
                .uri("?hl=en&gl=US&ceid=US:en&q=$encodedQuery")
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: return emptyList()

            val regex = Regex(
                "<item>\\s*<title><![CDATA[ (.*?) ]]></title>.*?<link>(.*?)</link>",
                RegexOption.DOT_MATCHES_ALL
            )

            regex.findAll(xml).take(5).map {
                mapOf(
                    "title" to it.groupValues[1].trim(),
                    "url" to it.groupValues[2].trim()
                )
            }.toList()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
