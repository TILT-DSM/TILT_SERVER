package org.example.tiltserver.realestate.domain

data class RealEstateSummary(
    val regionCode: String,
    val averagePrice: Int,
    val transactionCount: Int,
    val news: List<Map<String, String>>,
    val analysis: String
)
