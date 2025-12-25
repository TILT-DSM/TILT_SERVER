package org.example.tiltserver.gold.persistence.dto

import org.example.tiltserver.gold.entity.GoldPrice

data class GoldApiResponse(
    val data: GoldPrice,
    val analysis: String,
    val news: List<Map<String, String>>
)
