package org.example.tiltserver.coin.presentation.controller

import org.example.tiltserver.coin.facade.CoinFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coin")
class CoinController(
    private val coinFacade: CoinFacade
) {
    @GetMapping("/price/{coinSymbol}")
    fun getCoinInfo(@PathVariable coinSymbol: String) = coinFacade.getCoinInfo(coinSymbol)
}
