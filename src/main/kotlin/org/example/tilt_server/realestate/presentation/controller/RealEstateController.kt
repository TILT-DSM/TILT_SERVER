package org.example.tilt_server.realestate.presentation.controller

import org.example.tilt_server.realestate.domain.RealEstateSummary
import org.example.tilt_server.realestate.service.GetRealEstateSummaryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/realestate")
class RealEstateController(
    private val getRealEstateSummaryService: GetRealEstateSummaryService
) {

    @GetMapping("/{regionCode}")
    fun getRealEstateSummary(
        @PathVariable regionCode: String
    ): RealEstateSummary {
        return getRealEstateSummaryService.execute(regionCode)
    }
}
