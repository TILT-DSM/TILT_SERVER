package org.example.tiltserver.realestate.domain

data class RealEstateSummary(
    val regionCode: String,
    val averagePrice: Int, // 평균 거래가 (만원 기준)
    val transactionCount: Int // 거래 건수
)
