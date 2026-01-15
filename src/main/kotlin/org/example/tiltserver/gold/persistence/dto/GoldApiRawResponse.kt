package org.example.tiltserver.gold.persistence.dto

data class GoldApiRawResponse(
    val base: String?,
    val timestamp: Long?,
    val rates: Rates?
) {
    data class Rates(
        val XAU: Double?
    )
}
