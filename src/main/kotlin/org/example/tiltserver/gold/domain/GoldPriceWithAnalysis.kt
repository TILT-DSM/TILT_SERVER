package org.example.tiltserver.gold.domain

import org.example.tiltserver.gold.entity.GoldPrice

data class GoldPriceWithAnalysis(
    val goldPrice: GoldPrice,
    val analysis: String
)
