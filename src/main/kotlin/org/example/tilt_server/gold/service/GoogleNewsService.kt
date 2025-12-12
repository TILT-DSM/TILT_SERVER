package org.example.tilt_server.gold.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GoogleNewsService {

    private val client = WebClient.create("https://news.google.com/rss/search")

    fun getGoldNews(): List<Map<String, String>> {
        return try {
            val xml = client.get()
                .uri("?hl=en&gl=US&ceid=US:en&q=international+gold+price+OR+gold+market")
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: return emptyList()

            val regex = Regex(
                "<item>\\s*<title><!\\[CDATA\\[(.*?)]]></title>.*?<link>(.*?)</link>",
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

    fun getCoinNews(coinSymbol: String): List<Map<String, String>> {
        return try {
            val query = "${coinSymbol}+price+OR+${coinSymbol}+market"
            val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
            val xml = client.get()
                .uri("?hl=en&gl=US&ceid=US:en&q=$encodedQuery")
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: return emptyList()

            val regex = Regex(
                "<item>\\s*<title><!\\[CDATA\\[(.*?)]]></title>.*?<link>(.*?)</link>",
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
