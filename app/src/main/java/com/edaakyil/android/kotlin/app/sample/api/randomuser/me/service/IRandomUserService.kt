package com.edaakyil.android.kotlin.app.sample.api.randomuser.me.service

import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.dto.RandomUserInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRandomUserService {
    @GET("/")
    fun findUser(): Call<RandomUserInfo>

    @GET("/")
    fun findUser(@Query("results") count: Int): Call<RandomUserInfo>
}

// Full url -> https://api.randomuser.me/

// Full url -> https://api.randomuser.me/?results=2