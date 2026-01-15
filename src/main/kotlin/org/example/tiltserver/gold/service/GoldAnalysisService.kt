package org.example.tiltserver.gold.service

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

    @Value("\${openai.model:mistralai/mistral-7b-instruct:free}")
    private lateinit var model: String

    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val locks = ConcurrentHashMap<String, Any>()
    private val ttlNormalMillis = 10 * 60 * 1000L
    private val ttlTooManyMillis = 24 * 60 * 60 * 1000L

    fun analyze(price: Double, trend: String, diff: Double): String {
        val key = buildCacheKey(price, trend, diff)
        val now = System.currentTimeMillis()

        // Fast path: return cached result if within TTL.
        cache[key]?.takeIf { now < it.expiresAt }?.let { return it.value }

        val lock = locks.computeIfAbsent(key) { Any() }
        synchronized(lock) {
            val refreshedNow = System.currentTimeMillis()
            cache[key]?.takeIf { refreshedNow < it.expiresAt }?.let { return it.value }

            val (value, ttl) = try {
                val analysis = requestAnalysis(price, trend, diff)
                analysis to ttlNormalMillis
            } catch (e: WebClientResponseException) {
                if (e.statusCode.value() == 429) {
                    // Too many requests: cache for 24 hours to avoid further calls today.
                    "오늘 AI 요약 제공 한도 초과" to ttlTooManyMillis
                } else {
                    formatErrorMessage(e) to ttlNormalMillis
                }
            } catch (e: Exception) {
                formatErrorMessage(e) to ttlNormalMillis
            }

            // Cache the result so concurrent or repeat requests reuse it.
            cache[key] = CacheEntry(value, refreshedNow + ttl)
            return value
        }
    }

    private fun requestAnalysis(price: Double, trend: String, diff: Double): String {
        val prompt = """
?„ìž¬ êµ? œ ê¸??œì„¸??$${"%,.2f".format(price)} USD?…ë‹ˆ??
ìµœê·¼ 1ì£¼ì¼ ?‰ê·  ?€ë¹?${"%.2f".format(diff)}% ${trend}ë¥?ë³´ì´ê³??ˆìŠµ?ˆë‹¤.
???•ë³´ë¥?ë°”íƒ•?¼ë¡œ ê¸??œìž¥???ë¦„???ì„¸ ë¬¸ìž¥?¼ë¡œ ê°„ë‹¨???”ì•½?˜ì„¸??
?„ìž¬ ê¸ˆë¦¬, ?¬ëŸ¬ ?˜ìœ¨, ?¸í”Œ?ˆì´????ê²½ì œ ?”ì¸??ê³ ë ¤?˜ë˜
ë¶ˆí•„?”í•œ ?ˆì‹œ ë¬¸ìž¥?´ë‚˜ ê´„í˜¸, ?°ì˜´???†ì´ ?ì—°?¤ëŸ½ê²??‘ì„±?˜ì„¸??
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
            ?: "AI ë¶„ì„ ?¤íŒ¨"
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
        return "AI ë¶„ì„ ?¤íŒ¨ ($detail)"
    }

    private data class CacheEntry(
        val value: String,
        val expiresAt: Long
    )
}
