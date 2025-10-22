package org.example.tilt_server.gold.service

import org.example.tilt_server.gold.domain.GoldPriceWithAnalysis
import org.example.tilt_server.gold.persistence.adapter.GoldApiAdapter
import org.example.tilt_server.gold.port.`in`.GetGoldPriceUseCase
import org.example.tilt_server.gold.port.out.GoldPricePort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GetGoldPriceService(
    private val goldPricePort: GoldPricePort,
    private val goldAnalysisService: GoldAnalysisService
) : GetGoldPriceUseCase {

    override fun execute(): GoldPriceWithAnalysis {
        val today = goldPricePort.getGoldPrice()
        val goldApi = goldPricePort as? GoldApiAdapter

        val weekPrices = (1..7).mapNotNull { i ->
            val date = LocalDate.now().minusDays(i.toLong()).toString()
            try {
                goldApi?.getGoldPriceByDate(date)?.price
            } catch (_: Exception) {
                null
            }
        }

        val weekAvg = if (weekPrices.isNotEmpty()) weekPrices.average() else today.price
        val diff = ((today.price - weekAvg) / weekAvg) * 100

        val trend = when {
            diff > 0.5 -> "상승세"
            diff < -0.5 -> "하락세"
            else -> "안정세"
        }

        val analysis = goldAnalysisService.analyze(today.price, trend, diff)

        return GoldPriceWithAnalysis(today, analysis)
    }
}
