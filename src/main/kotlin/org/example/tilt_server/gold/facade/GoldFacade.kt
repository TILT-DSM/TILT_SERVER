package org.example.tilt_server.gold.facade

import org.example.tilt_server.gold.service.GetGoldPriceService
import org.example.tilt_server.gold.service.GoogleNewsService
import org.springframework.stereotype.Component

@Component
class GoldFacade(
    private val getGoldPriceService: GetGoldPriceService,
    private val googleNewsService: GoogleNewsService
) {
    fun getGoldInfo(): Map<String, Any> {
        val result = getGoldPriceService.execute()
        val news = googleNewsService.getGoldNews()

        return mapOf(
            "data" to result.data,
            "analysis" to result.analysis,
            "news" to news
        )
    }
}
