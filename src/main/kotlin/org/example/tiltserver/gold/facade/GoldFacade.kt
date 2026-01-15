package org.example.tiltserver.gold.facade

import org.example.tiltserver.gold.persistence.dto.GoldApiResponse
import org.example.tiltserver.gold.service.GetGoldPriceService
import org.example.tiltserver.gold.service.GoldNewsService
import org.springframework.stereotype.Component

@Component
class GoldFacade(
    private val getGoldPriceService: GetGoldPriceService,
    private val goldNewsService: GoldNewsService
) {

    fun getGoldInfo(): GoldApiResponse {
        val result = getGoldPriceService.execute()
        val news = goldNewsService.getGoldNews()

        return GoldApiResponse(
            data = result.goldPrice,
            analysis = result.analysis,
            news = news
        )
    }
}
