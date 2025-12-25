package org.example.tiltserver.gold.persistence.dto

data class GoldApiRawResponse(
    val price: Double?,
    val currency: String?,
    val timestamp: Long?,
    val open: Double?,
    val high: Double?,
    val low: Double?
)
