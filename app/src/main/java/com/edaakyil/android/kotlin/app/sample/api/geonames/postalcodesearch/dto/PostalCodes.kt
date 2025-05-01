package com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.dto

import com.google.gson.annotations.SerializedName

data class PostalCodes(
    @SerializedName("postalcodes") val postalCodes: List<PostalCode>
)