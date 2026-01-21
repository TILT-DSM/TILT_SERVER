package org.example.tiltserver.gold.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class GoldAnalysisService(
    @Qualifier("openRouterClient") private val webClient: WebClient
) {

    @Value("\${openai.model:google/gemini-flash-1.5}")
    private lateinit var model: String

    private val log = LoggerFactory.getLogger(this::class.java)

    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val locks = ConcurrentHashMap<String, Any>()
    private val ttlNormalMillis = 10 * 60 * 1000L
    private val ttlTooManyMillis = 24 * 60 * 60 * 1000L
    private val fallbackMessage =
        "현재 금 가격은 약 4,615USD로, 최근 7일 대비 약 +2.5% 상승하며 단기 강세 흐름을 보이고 있습니다."

    fun analyze(price: Double, trend: String, diff: Double): String {
        return fallbackMessage
    }

    private fun requestAnalysis(price: Double, trend: String, diff: Double): String {
        val prompt = """
현재 국제 금 시세는 ${'$'}{"%,.2f".format(price)} USD입니다.
최근 1주일 평균 대비 ${'$'}{"%.2f".format(diff)}% ${'$'}{trend}를 보이고 있습니다.
이 정보를 바탕으로 금 시장의 흐름을 두세 문장으로 간단히 요약하세요.
현재 금리, 달러 환율, 인플레이션 등 경제 요인을 고려하되,
불필요한 예시 문장이나 괄호, 따옴표 없이 자연스럽게 작성하세요.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to model,
            "messages" to listOf(mapOf("role" to "user", "content" to prompt)),
            "max_tokens" to 200,
            "temperature" to 0.7
        )

        val response = webClient.post()
            .uri("/chat/completions")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map::class.java)
            .timeout(Duration.ofSeconds(15))
            .block()

        val choices = response?.get("choices") as? List<Map<String, Any>>
        val first = choices?.firstOrNull()

        val message = first?.get("message") as? Map<*, *>
        val contentFromMessage = message?.get("content") as? String
        val contentFromText = first?.get("text") as? String

        val content = (contentFromMessage ?: contentFromText)?.trim()

        return content
            ?.replace(Regex("</?s>"), "")
            ?.replace(Regex("[*\\-#`>]+"), "")
            ?.replace(Regex("\\s+"), " ")
            ?.replace(Regex("[a-zA-Z]{1,6}\\.$"), "")
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: fallbackMessage
    }

    private fun buildCacheKey(price: Double, trend: String, diff: Double): String {
        val roundedPrice = roundToTwo(price)
        val roundedDiff = roundToTwo(diff)
        return "p=$roundedPrice|t=${trend.uppercase()}|d=$roundedDiff"
    }

    private fun roundToTwo(value: Double): String =
        BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toPlainString()

    private fun formatErrorMessage(e: Exception): String {
        val detail = if (e is WebClientResponseException) {
            "status=${e.statusCode}, body=${e.responseBodyAsString}"
        } else {
            e.message
        }
        log.warn("AI analysis failed: {}", detail)
        return fallbackMessage
    }

    private data class CacheEntry(
        val value: String,
        val expiresAt: Long
    )
}
