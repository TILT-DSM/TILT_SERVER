package org.example.tilt_server.realestate.service

import org.example.tilt_server.realestate.domain.RealEstatePrice
import org.example.tilt_server.realestate.port.`in`.GetRealEstatePriceUseCase
import org.example.tilt_server.realestate.port.out.RealEstatePort
import org.springframework.stereotype.Service

@Service
class GetRealEstatePriceService(
    private val realEstatePort: RealEstatePort
) : GetRealEstatePriceUseCase {

    override fun execute(regionCode: String): RealEstatePrice {
        return realEstatePort.getRealEstatePrice(regionCode)
    }
}