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

            val formattedDiff = String.format("%.2f", diff)
            val result = when (trend) {
                "상승세" -> "최근 환율은 1주일 평균보다 $formattedDiff% 높은 상승 추세입니다."
                "하락세" -> "최근 환율은 1주일 평균보다 $formattedDiff% 낮은 하락 추세입니다."
                else -> "최근 환율은 1주일 평균과 비슷한 안정적인 추세입니다."
            }

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
