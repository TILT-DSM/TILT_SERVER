package org.example.tilt_server.realestate.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.tilt_server.global.util.XmlToJsonConverter
import org.example.tilt_server.realestate.domain.RealEstateSummary
import org.example.tilt_server.realestate.persistence.adapter.RealEstateApiAdapter
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class GetRealEstateSummaryService(
    private val realEstateApiAdapter: RealEstateApiAdapter,
    private val objectMapper: ObjectMapper
) {

    fun execute(regionCode: String): RealEstateSummary {

        val lawdCd = mapRegionCode(regionCode)

        val dealYmd = LocalDate.now()
            .minusMonths(1)
            .format(DateTimeFormatter.ofPattern("yyyyMM"))

        val rawXml = realEstateApiAdapter.fetchAptTradeRawData(
            lawdCd = lawdCd,
            dealYmd = dealYmd
        )

        val json = XmlToJsonConverter.convert(rawXml)
        val root = objectMapper.readTree(json)

        val itemsNode = root
            .path("response")
            .path("body")
            .path("items")
            .path("item")

        var totalPrice = 0
        var count = 0

        if (itemsNode.isArray) {
            for (item in itemsNode) {
                val priceText = item
                    .path("dealAmount")
                    .asText()
                    .replace(",", "")
                    .trim()

                if (priceText.isNotEmpty()) {
                    totalPrice += priceText.toInt()
                    count++
                }
            }
        }

        val averagePrice =
            if (count > 0) totalPrice / count else 0

        return RealEstateSummary(
            regionCode = regionCode,
            averagePrice = averagePrice,
            transactionCount = count
        )
    }

    private fun mapRegionCode(regionCode: String): String =
        when (regionCode.uppercase()) {
            "SEOUL" -> "11680"
            "BUSAN" -> "26440"
            "DAEGU" -> "27200"
            else -> throw IllegalArgumentException("지원하지 않는 지역: $regionCode")
        }
}
