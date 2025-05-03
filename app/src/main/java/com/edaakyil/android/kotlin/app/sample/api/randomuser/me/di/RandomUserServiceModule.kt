package com.edaakyil.android.kotlin.app.sample.api.randomuser.me.di

import com.edaakyil.android.kotlin.app.sample.api.constant.RANDOM_USER
import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.service.IRandomUserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RandomUserServiceModule {
    @Provides
    @Singleton
    fun provideRandomUserService(@Named(RANDOM_USER) retrofit: Retrofit): IRandomUserService = retrofit.create(IRandomUserService::class.java)
}