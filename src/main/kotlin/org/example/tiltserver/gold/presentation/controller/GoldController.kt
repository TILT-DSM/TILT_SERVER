package org.example.tiltserver.gold.presentation.controller

import org.example.tiltserver.gold.entity.GoldPrice
import org.example.tiltserver.gold.service.GetGoldPriceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gold")
class GoldController(
    private val goldService: GetGoldPriceService
) {
    @GetMapping("/price")
    fun getGold(): GoldPrice = goldService.execute().goldPrice
}
