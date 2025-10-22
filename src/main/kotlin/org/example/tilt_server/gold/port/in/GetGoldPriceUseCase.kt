package org.example.tilt_server.gold.port.`in`

import org.example.tilt_server.gold.domain.GoldPriceWithAnalysis

interface GetGoldPriceUseCase {
    fun execute(): GoldPriceWithAnalysis
}