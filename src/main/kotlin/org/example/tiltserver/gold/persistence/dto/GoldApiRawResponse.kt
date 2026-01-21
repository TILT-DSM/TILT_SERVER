package org.example.tiltserver.gold.persistence.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GoldApiRawResponse(
    val success: Boolean?,
    val base: String?,
    val timestamp: Long?,
    val rates: Rates?
) {
    data class Rates(
        val XAU: Double?
    )
}
