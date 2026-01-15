package org.example.tiltserver.coin.service

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ConcurrentHashMap

@Service
class CoinAnalysisService {

    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val locks = ConcurrentHashMap<String, Any>()
    private val ttlMillis = 10 * 60 * 1000L

    fun analyze(price: Double, trend: String, diff: Double): String {
        val key = buildCacheKey(price, trend, diff)
        val now = System.currentTimeMillis()
        cache[key]?.takeIf { now < it.expiresAt }?.let { return it.value }

        val lock = locks.computeIfAbsent(key) { Any() }
        synchronized(lock) {
            val refreshedNow = System.currentTimeMillis()
            cache[key]?.takeIf { refreshedNow < it.expiresAt }?.let { return it.value }

            val sentiment = when {
                diff > 0.5 -> "긍정적"
                diff < -0.5 -> "부정적"
                else -> "중립적"
            }
            val result =
                "현재 가격 $price USD로, 최근 7일 대비 ${String.format("%.2f", diff)}% 변동하여 " +
                    "${trend}인 $sentiment 흐름을 보이고 있습니다."

            // Cache for 10 minutes to avoid repeated computation.
            cache[key] = CacheEntry(result, refreshedNow + ttlMillis)
            return result
        }
    }

    private fun buildCacheKey(price: Double, trend: String, diff: Double): String {
        val roundedPrice = roundToTwo(price)
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
