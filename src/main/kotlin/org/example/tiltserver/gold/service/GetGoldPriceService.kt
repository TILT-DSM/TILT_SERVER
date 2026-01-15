package org.example.tiltserver.gold.service

import org.example.tiltserver.gold.domain.GoldPriceWithAnalysis
import org.example.tiltserver.gold.entity.GoldPrice
import org.example.tiltserver.gold.port.`in`.GetGoldPriceUseCase
import org.example.tiltserver.gold.port.out.GoldPricePort
import org.springframework.stereotype.Service

@Service
class GetGoldPriceService(
    private val goldPricePort: GoldPricePort,
    private val goldAnalysisService: GoldAnalysisService
) : GetGoldPriceUseCase {

    private val cacheTtlMillis = 5 * 60 * 1000L
    private val cacheLock = Any()

    @Volatile
    private var cachedPrice: GoldPrice? = null

    @Volatile
    private var cacheExpiresAt: Long = 0L

    override fun execute(): GoldPriceWithAnalysis {
        val today = getCachedGoldPrice()
        val weekAvg = today.price
        val diff = ((today.price - weekAvg) / weekAvg) * 100

        val trend = when {
            diff > 0.5 -> "up"
            diff < -0.5 -> "down"
            else -> "flat"
        }

        val analysis = goldAnalysisService.analyze(today.price, trend, diff)

        return GoldPriceWithAnalysis(today, analysis)
    }

    private fun getCachedGoldPrice(): GoldPrice {
        val now = System.currentTimeMillis()
        val current = cachedPrice
        if (current != null && now < cacheExpiresAt) {
            return current
        }

        synchronized(cacheLock) {
            val refreshedNow = System.currentTimeMillis()
            val refreshedCurrent = cachedPrice
            if (refreshedCurrent != null && refreshedNow < cacheExpiresAt) {
                return refreshedCurrent
            }

            val fresh = goldPricePort.getGoldPrice()
            cachedPrice = fresh
            cacheExpiresAt = refreshedNow + cacheTtlMillis
            return fresh
        }
    }
}
