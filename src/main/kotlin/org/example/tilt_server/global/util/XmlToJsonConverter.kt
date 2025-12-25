package org.example.tilt_server.global.util

object XmlToJsonConverter {

    fun convert(xml: String): String {
        val jsonObject = org.json.XML.toJSONObject(xml)
        return jsonObject.toString()
    }
}

