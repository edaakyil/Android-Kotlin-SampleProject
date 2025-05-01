package com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.service

import com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.dto.PostalCodes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IPostalCodeService {
    @GET("postalCodeLookupJSON")
    fun findByPostalCode(
        @Query("username") username: String,
        @Query("country") country: String,
        @Query("postalcode") postalcode: String
    ): Call<PostalCodes>
}



// Full url -> http://api.geonames.org/postalCodeLookupJSON?formatted=true&postalcode=34843&country=TR&username=csystem&style=full
// Base url -> http://api.geonames.org/postalCodeLookupJSON or http://api.geonames.org/(this is the left of the question mark(?))
// Get parameters -> formatted=true&postalcode=34843&country=TR&username=csystem&style=full (this is the right of the question mark(?))
// Get parameters we need -> postalcode=34843&country=TR&username=csystem
