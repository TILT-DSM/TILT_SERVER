package org.example.tilt_server.gold.port.out

import org.example.tilt_server.gold.entity.GoldPrice

interface GoldPricePort {
    fun getGoldPrice(): GoldPrice
}