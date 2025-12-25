package org.example.tiltserver.gold.port.`in`

import org.example.tiltserver.gold.domain.GoldPriceWithAnalysis

interface GetGoldPriceUseCase {
    fun execute(): GoldPriceWithAnalysis
}
