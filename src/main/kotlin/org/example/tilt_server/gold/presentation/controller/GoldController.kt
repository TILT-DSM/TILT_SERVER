package org.example.tilt_server.gold.presentation.controller

import org.example.tilt_server.gold.facade.GoldFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gold")
class GoldController (
    private val goldFacade: GoldFacade
){
    @GetMapping("/price")
    fun getGold() = goldFacade.getGoldInfo()
}