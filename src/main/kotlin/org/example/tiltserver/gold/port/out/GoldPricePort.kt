package org.example.tiltserver.gold.port.out

import org.example.tiltserver.gold.entity.GoldPrice

interface GoldPricePort {
    fun getGoldPrice(): GoldPrice
}
