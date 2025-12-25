package org.example.tiltserver.coin.service

import org.example.tiltserver.coin.domain.CoinPriceWithAnalysis
import org.example.tiltserver.coin.port.`in`.GetCoinPriceUseCase
import org.example.tiltserver.coin.port.out.CoinPricePort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GetCoinPriceService(
    private val coinPricePort: CoinPricePort,
    private val coinAnalysisService: CoinAnalysisService
) : GetCoinPriceUseCase {

    override fun execute(coinSymbol: String): CoinPriceWithAnalysis {
        val today = coinPricePort.getCoinPrice(coinSymbol)

        val weekPrices = (1..7).mapNotNull { i ->
            val date = LocalDate.now().minusDays(i.toLong()).toString()
            try {
                coinPricePort.getCoinPriceByDate(coinSymbol, date)?.price
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

        val analysis = coinAnalysisService.analyze(today.price, trend, diff)

        return CoinPriceWithAnalysis(today, analysis)
    }
}
