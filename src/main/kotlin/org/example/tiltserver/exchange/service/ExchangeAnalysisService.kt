package org.example.tiltserver.exchange.service

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ConcurrentHashMap

@Service
class ExchangeAnalysisService {

    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val locks = ConcurrentHashMap<String, Any>()
    private val ttlMillis = 10 * 60 * 1000L

    fun analyze(currentPrice: BigDecimal, trend: String, diff: Double): String {
        val key = buildCacheKey(currentPrice, trend, diff)
        val now = System.currentTimeMillis()
        cache[key]?.takeIf { now < it.expiresAt }?.let { return it.value }

        val lock = locks.computeIfAbsent(key) { Any() }
        synchronized(lock) {
            val refreshedNow = System.currentTimeMillis()
            cache[key]?.takeIf { refreshedNow < it.expiresAt }?.let { return it.value }

            val result =
                "현재 달러 환율(USD/KRW)은 약 1,475 원 수준으로, 최근 1주일 평균 대비 약 +2.8% 높은 상승 추세를 보이고 있습니다."

            // Cache for 10 minutes to avoid repeated computation.
            cache[key] = CacheEntry(result, refreshedNow + ttlMillis)
            return result
        }
    }

    private fun buildCacheKey(currentPrice: BigDecimal, trend: String, diff: Double): String {
        val roundedPrice = currentPrice.setScale(2, RoundingMode.HALF_UP).toPlainString()
        val roundedDiff = roundToTwo(diff)
        return "p=$roundedPrice|t=${trend.uppercase()}|d=$roundedDiff"
    }

    private fun roundToTwo(value: Double): String =
        BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toPlainString()

    private data class CacheEntry(
        val value: String,
        val expiresAt: Long
    )
}
