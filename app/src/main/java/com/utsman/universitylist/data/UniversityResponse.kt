package com.utsman.universitylist.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UniversityResponse(
    @Json(name = "state-province")
    val stateProvince: Any? = null,
    @Json(name = "alpha_two_code")
    val alphaTwoCode: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "domains")
    val domains: List<String?>? = null,
    @Json(name = "web_pages")
    val webPages: List<String?>? = null,
    @Json(name = "country")
    val country: String? = null
)