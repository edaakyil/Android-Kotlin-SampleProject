package com.edaakyil.android.kotlin.app.sample.api.di.module

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime

@Module
@InstallIn(ActivityComponent::class)
object LocalDateTimeModule {
    @Provides
    fun provideCurrentDateTime(@ApplicationContext context: Context): LocalDateTime {
        //Log.i("date-time-module", "Created provideDateTime")
        //Toast.makeText(context, "provideDateTime", Toast.LENGTH_LONG).show()

        return LocalDateTime.now()
    }
}