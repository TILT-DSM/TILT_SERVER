package org.example.tiltserver.coin.service

import org.springframework.stereotype.Service

@Service
class CoinAnalysisService {
    fun analyze(price: Double, trend: String, diff: Double): String {
        val sentiment = when {
            diff > 0.5 -> "긍정적"
            diff < -0.5 -> "부정적"
            else -> "중립적"
        }
        return "현재 가격 $price USD로, 최근 7일 대비 ${String.format("%.2f", diff)}% 변동하여 ${trend}인 $sentiment 흐름을 보이고 있습니다."
    }
}
