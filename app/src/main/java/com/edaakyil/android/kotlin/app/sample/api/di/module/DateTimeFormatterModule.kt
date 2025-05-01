package com.edaakyil.android.kotlin.app.sample.api.di.module

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DateTimeFormatterModule {
    @Provides
    @Singleton
    fun provideDateTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        //Log.i("date-time-formatter-module", "Created provideDateTimeFormatter")
        //Toast.makeText(context, "provideDateTimeFormatter", Toast.LENGTH_LONG).show()

        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    }
}