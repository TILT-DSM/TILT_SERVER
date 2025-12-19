package org.example.tilt_server.exchange.presentation.controller

import org.example.tilt_server.exchange.facade.ExchangeFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exchange")
class ExchangeController(
    private val exchangeFacade: ExchangeFacade
) {

    @GetMapping("/{currencyCode}")
    fun getExchangeRate(@PathVariable currencyCode: String): ResponseEntity<Map<String, Any>> {
        val result = exchangeFacade.getExchangeRateInfo(currencyCode.uppercase())
        return ResponseEntity.ok(result)
    }
}
