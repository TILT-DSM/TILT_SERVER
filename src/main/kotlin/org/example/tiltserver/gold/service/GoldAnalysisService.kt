package org.example.tiltserver.gold.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class GoldAnalysisService(
    @Qualifier("openRouterClient") private val webClient: WebClient
) {

    @Value("\${openai.model:mistralai/mistral-7b-instruct:free}")
    private lateinit var model: String

    fun analyze(price: Double, trend: String, diff: Double): String {
        val prompt = """
현재 국제 금 시세는 ${"$"}{"%,.2f".format(price)} USD입니다.
최근 1주일 평균 대비 ${"$"}{"%.2f".format(diff)}% ${trend}를 보이고 있습니다.
이 정보를 바탕으로 금 시장의 흐름을 두세 문장으로 간단히 요약하세요.
현재 금리, 달러 환율, 인플레이션 등 경제 요인을 고려하되
불필요한 예시 문장이나 괄호, 따옴표 없이 자연스럽게 작성하세요.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to model,
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to prompt
                )
            ),
            "max_tokens" to 200,
            "temperature" to 0.7
        )

        return try {
            val response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map::class.java)
                .block()

            val choices = response?.get("choices") as? List<Map<String, Any>>
            val message = choices
                ?.firstOrNull()
                ?.get("message") as? Map<String, Any>

            val content = message?.get("content") as? String

            content
                ?.replace(Regex("[*\\-#`>]+"), "")
                ?.replace(Regex("\\s+"), " ")
                ?.trim()
                ?: "AI 분석 실패"
        } catch (e: Exception) {
            e.printStackTrace()
            "AI 분석 실패 (${e.message})"
        }
    }
}
