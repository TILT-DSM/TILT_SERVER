package org.example.tiltserver.realestate.port.out

interface RealEstatePort {

    fun fetchAptTradeRawData(
        lawdCd: String,
        dealYmd: String
    ): String
}
