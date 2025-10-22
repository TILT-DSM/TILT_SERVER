package org.example.tilt_server.gold.domain

import org.example.tilt_server.gold.entity.GoldPrice

data class GoldPriceWithAnalysis(
    val data: GoldPrice,
    val analysis: String
)