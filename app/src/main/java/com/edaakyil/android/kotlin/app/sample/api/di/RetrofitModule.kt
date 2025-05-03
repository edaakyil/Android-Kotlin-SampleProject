package com.edaakyil.android.kotlin.app.sample.api.di

import com.edaakyil.android.kotlin.app.sample.api.constant.BASE_URL_GEONAMES
import com.edaakyil.android.kotlin.app.sample.api.constant.BASE_URL_RANDOMUSER
import com.edaakyil.android.kotlin.app.sample.api.constant.GEONAMES
import com.edaakyil.android.kotlin.app.sample.api.constant.RANDOM_USER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    @Named(RANDOM_USER)
    fun provideRandomUserRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_RANDOMUSER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named(GEONAMES)
    fun provideGeonamesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_GEONAMES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

