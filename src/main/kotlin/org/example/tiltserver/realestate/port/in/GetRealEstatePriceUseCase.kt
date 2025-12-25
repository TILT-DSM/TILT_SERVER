package org.example.tiltserver.realestate.port.`in`

import org.example.tiltserver.realestate.domain.RealEstateSummary

interface GetRealEstatePriceUseCase {
    fun execute(regionCode: String): RealEstateSummary
}
