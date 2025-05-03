package com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.di

import com.edaakyil.android.kotlin.app.sample.api.constant.GEONAMES
import com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.service.IPostalCodeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

// API servisi elde etmek için
@Module
@InstallIn(SingletonComponent::class)
object PostalCodeServiceModule {
    @Provides
    @Singleton
    fun providePostalCodeService(@Named(GEONAMES) retrofit: Retrofit): IPostalCodeService = retrofit.create(IPostalCodeService::class.java)
}
