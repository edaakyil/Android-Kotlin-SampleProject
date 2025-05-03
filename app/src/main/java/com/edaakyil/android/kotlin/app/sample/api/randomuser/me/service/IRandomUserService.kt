package com.edaakyil.android.kotlin.app.sample.api.randomuser.me.service

import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.dto.RandomUserInfo
import retrofit2.Call
import retrofit2.http.GET

interface IRandomUserService {
    @GET("/")
    fun findUser(): Call<RandomUserInfo>
}

// Full url -> https://api.randomuser.me/