package org.example.tiltserver.realestate.domain

data class RealEstateSummary(
    val regionCode: String,
    val averagePrice: Int,
    val transactionCount: Int,
    val analysis: String,
    val news: List<Map<String, String>>
)
