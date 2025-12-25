package org.example.tiltserver.exchange.persistence.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ExchangeApiResponse(
    @JsonProperty("result")
    val result: Int, // 1: success, 2: data code error, 3: auth error, 4: option error

    @JsonProperty("cur_unit")
    val currencyUnit: String,

    @JsonProperty("cur_nm")
    val currencyName: String,

    @JsonProperty("ttb")
    val ttb: String, // 전신환(송금) 받으실때

    @JsonProperty("tts")
    val tts: String, // 전신환(송금) 보내실때

    @JsonProperty("deal_bas_r")
    val dealingBaseRate: String, // 매매 기준율

    @JsonProperty("bkpr")
    val bookPrice: String, // 장부가격

    @JsonProperty("yy_efee_r")
    val yearEfeeRate: String, // 년환가료율

    @JsonProperty("ten_dd_efee_r")
    val tenDayEfeeRate: String, // 10일환가료율

    @JsonProperty("kftc_bkpr")
    val kftcBookPrice: String, // 서울외국환중개 장부가격

    @JsonProperty("kftc_deal_bas_r")
    val kftcDealingBaseRate: String // 서울외국환중개 매매기준율
)
