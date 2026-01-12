package org.example.tiltserver.global.filter

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class OriginLoggingFilter : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val origin = exchange.request.headers.getFirst("Origin")
        if (origin != null) {
            println(">>> Incoming Origin: $origin")
        }
        return chain.filter(exchange)
    }
}
