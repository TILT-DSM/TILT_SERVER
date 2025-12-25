package org.example.tilt_server.gold.persistence.dto

import org.example.tilt_server.gold.entity.GoldPrice

data class GoldApiResponse(
    val data: GoldPrice,
    val analysis: String,
    val news: List<Map<String, String>>
)
