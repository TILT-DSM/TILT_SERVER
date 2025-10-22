package org.example.tilt_server.gold.service

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

    fun analyze(price: Double): String {
        val prompt = "현재 금 시세는 ${price} USD입니다. 시장 상황을 한 줄로 분석해줘. (예: 상승세, 하락세, 안정적 등)"

        val isChatModel = model.contains("gpt") || model.contains("chat")

        val requestBody = if (isChatModel) {
            mapOf(
                "model" to model,
                "messages" to listOf(
                    mapOf("role" to "user", "content" to prompt)
                )
            )
        } else {
            mapOf(
                "model" to model,
                "prompt" to prompt,
                "max_tokens" to 100
            )
        }

        val endpoint = if (isChatModel) "/chat/completions" else "/completions"

        return try {
            val response = webClient.post()
                .uri(endpoint)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map::class.java)
                .block()

            val choices = response?.get("choices") as? List<Map<String, Any>>

            val content = if (isChatModel) {
                val message = choices?.firstOrNull()?.get("message") as? Map<String, Any>
                message?.get("content") as? String
            } else {
                choices?.firstOrNull()?.get("text") as? String
            }

            content?.trim() ?: "AI 분석 실패 (응답 없음)"
        } catch (e: Exception) {
            e.printStackTrace()
            "AI 분석 실패 (${e.message})"
        }
    }

}
