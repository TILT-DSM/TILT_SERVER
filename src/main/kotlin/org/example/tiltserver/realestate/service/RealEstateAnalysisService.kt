package org.example.tiltserver.realestate.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RealEstateAnalysisService(
    @Qualifier("openRouterClient") private val webClient: WebClient
) {

    @Value("\${openai.model:mistralai/mistral-7b-instruct:free}")
    private lateinit var model: String

    fun analyze(
        regionCode: String,
        averagePrice: Int,
        transactionCount: Int,
        newsTitles: List<String>
    ): String {
        // 🔥 숫자는 미리 계산해서 문자열로 만든다
        val formattedPrice = "%,d".format(averagePrice)

        val newsContext =
            if (newsTitles.isNotEmpty()) {
                newsTitles.joinToString(separator = "\n- ", prefix = "- ")
            } else {
                "최근 관련 뉴스 없음"
            }

        val prompt = """
지역: $regionCode
최근 아파트 평균 거래가는 ${formattedPrice}만원이며,
총 거래 건수는 ${transactionCount}건입니다.

최근 부동산 관련 뉴스:
$newsContext

위 정보를 바탕으로 해당 지역 부동산 시장의 흐름을
두세 문장으로 자연스럽게 분석하세요.
금리, 거래량, 시장 심리를 고려하되
코드 표현, 기호, 변수명, 수식은 절대 사용하지 마세요.
        """.trimIndent()

        val requestBody = mapOf(
            "model" to model,
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to prompt
                )
            ),
            "max_tokens" to 250,
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
            "AI 분석 실패 (${e.message})"
        }
    }
}
