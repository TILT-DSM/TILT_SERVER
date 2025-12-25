package org.example.tilt_server.realestate.port.out

interface RealEstatePort {

    fun fetchAptTradeRawData(
        lawdCd: String,
        dealYmd: String
    ): String
}
