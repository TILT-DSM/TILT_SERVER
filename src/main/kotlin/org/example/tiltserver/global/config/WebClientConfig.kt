package org.example.tiltserver.global.config

import io.netty.channel.ChannelOption
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig(
    @Value("\${spring.webclient.connect-timeout}") private val connectTimeout: Int,
    @Value("\${spring.webclient.read-timeout}") private val readTimeout: Long
) {

    @Bean
    fun defaultWebClient(): WebClient {
        // For SSL handshake/trust issues during development/testing
        // WARNING: This trusts all certificates and is insecure. Do not use in production.
        val sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build()

        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
            .doOnConnected {
                it.addHandlerLast(ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
            }
            .secure { it.sslContext(sslContext) }

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }

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
