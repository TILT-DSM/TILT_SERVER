package org.example.tilt_server.realestate.port.out

import org.example.tilt_server.realestate.domain.RealEstatePrice

interface RealEstatePort {
    fun getRealEstatePrice(regionCode: String): RealEstatePrice
}