package org.example.tilt_server.realestate.presentation.controller

import org.example.tilt_server.realestate.domain.RealEstatePrice
import org.example.tilt_server.realestate.port.`in`.GetRealEstatePriceUseCase
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/realestate")
class RealEstateController(
    private val getRealEstatePriceUseCase: GetRealEstatePriceUseCase
) {

    @GetMapping("/{regionCode}")
    fun getRealEstatePrice(@PathVariable regionCode: String): RealEstatePrice {
        return getRealEstatePriceUseCase.execute(regionCode)
    }
}
