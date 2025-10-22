package org.example.tilt_server.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun defaultWebClient(): WebClient =
        WebClient.builder().build()

    @Bean("openRouterClient")
    fun openRouterWebClient(
        @Value("\${openai.key}") openRouterKey: String
    ): WebClient =
        WebClient.builder()
            .baseUrl("https://openrouter.ai/api/v1")
            .defaultHeader("Authorization", "Bearer $openRouterKey")
            .defaultHeader("Content-Type", "application/json")
            .build()
}
