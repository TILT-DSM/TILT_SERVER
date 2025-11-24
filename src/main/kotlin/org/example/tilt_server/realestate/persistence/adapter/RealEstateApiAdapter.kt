package org.example.tilt_server.realestate.persistence.adapter

import org.example.tilt_server.realestate.domain.RealEstatePrice
import org.example.tilt_server.realestate.port.out.RealEstatePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class RealEstateApiAdapter(
    @Value("\${realstate.api.key}") private val apiKey: String,
    private val webClientBuilder: WebClient.Builder
): RealEstatePort {

    private val client = WebClient.builder()
        .baseUrl("https://api.odcloud.kr/api/15058249/v1")
        .build()

    override fun getRealEstatePrice(regionCode: String): RealEstatePrice {
        val response = client.get()
            .uri {
                it.path("/getRTMSDataSvcAptTradeDev")
                    .queryParam("LAWD_CD", regionCode)
                    .queryParam("DEAL_YMD", "202510")
                    .queryParam("serviceKey", apiKey)
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        return RealEstatePrice(regionCode = regionCode, rawData = response ?: "no data")
    }

}