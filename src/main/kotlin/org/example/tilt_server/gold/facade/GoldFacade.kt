package org.example.tilt_server.gold.facade

import org.example.tilt_server.gold.service.GetGoldPriceService
import org.springframework.stereotype.Component

@Component
class GoldFacade(
    private val getGoldPriceService: GetGoldPriceService

) {
    fun getGoldInfo() = getGoldPriceService.execute()
}