package org.example.tiltserver.realestate.presentation.controller

import org.example.tiltserver.realestate.domain.RealEstateSummary
import org.example.tiltserver.realestate.service.GetRealEstateSummaryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
