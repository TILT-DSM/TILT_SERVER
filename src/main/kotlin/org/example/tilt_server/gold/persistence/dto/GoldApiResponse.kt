package org.example.tilt_server.gold.persistence.dto

data class GoldApiResponse(
    val price: Double?,
    val currency: String?,
    val timestamp: Long?,
    val open: Double?,
    val high: Double?,
    val low: Double?
)