package org.example.tilt_server.gold.service

import org.example.tilt_server.gold.domain.GoldPriceWithAnalysis
import org.example.tilt_server.gold.port.`in`.GetGoldPriceUseCase
import org.example.tilt_server.gold.port.out.GoldPricePort
import org.springframework.stereotype.Service

@Service
class GetGoldPriceService(
    private val goldPricePort: GoldPricePort,
    private val goldAnalysisService: GoldAnalysisService
) : GetGoldPriceUseCase {
    override fun execute(): GoldPriceWithAnalysis {
        val gold = goldPricePort.getGoldPrice()
        val summary = goldAnalysisService.analyze(gold.price)
        return GoldPriceWithAnalysis(gold, summary)
    }
}