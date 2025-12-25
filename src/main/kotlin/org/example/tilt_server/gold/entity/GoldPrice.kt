package org.example.tilt_server.gold.entity

data class GoldPrice(
    val price: Double,
    val currency: String,
    val timestamp: Long,
    val open: Double?,
    val high: Double?,
    val low: Double?
)
