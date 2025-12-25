package org.example.tiltserver.exchange.service

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ExchangeAnalysisService {
    fun analyze(currentPrice: BigDecimal, trend: String, diff: Double): String {
        val formattedDiff = String.format("%.2f", diff)
        return when (trend) {
            "상승세" -> "최근 환율은 1주일 평균보다 $formattedDiff% 높은 상승 추세입니다."
            "하락세" -> "최근 환율은 1주일 평균보다 $formattedDiff% 낮은 하락 추세입니다."
            else -> "최근 환율은 1주일 평균과 비슷한 안정적인 추세입니다."
        }
    }
}
