package org.example.tiltserver.realestate.persistence.adapter

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class RealEstateApiAdapter(
    @Value("\${realestate.api.key}") private val apiKey: String
) {

    private val client = WebClient.builder()
        .baseUrl("https://apis.data.go.kr")
        .build()

    fun fetchAptTradeRawData(
        lawdCd: String,
        dealYmd: String
    ): String {
        return client.get()
            .uri {
                it.path("/1613000/RTMSDataSvcAptTrade/getRTMSDataSvcAptTrade")
                    .queryParam("serviceKey", apiKey)
                    .queryParam("LAWD_CD", lawdCd)
                    .queryParam("DEAL_YMD", dealYmd)
                    .queryParam("numOfRows", 1000)
                    .queryParam("pageNo", 1)
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()
            ?: throw RuntimeException("국토부 API 응답 없음")
    }
}
