package org.example.tilt_server.realestate.port.`in`

import org.example.tilt_server.realestate.domain.RealEstateSummary

interface GetRealEstatePriceUseCase {
    fun execute(regionCode: String): RealEstateSummary
}