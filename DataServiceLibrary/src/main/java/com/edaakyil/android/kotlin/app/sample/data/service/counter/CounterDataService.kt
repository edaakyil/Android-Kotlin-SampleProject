package com.edaakyil.android.kotlin.app.sample.data.service.counter

import android.content.Context
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.DateTimeFormatterENInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedWriter
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val FILE_NAME = "counter.txt"

class CounterDataService @Inject constructor(
    @ApplicationContext context: Context,
    @DateTimeFormatterENInterceptor dateTimeFormatter: DateTimeFormatter
) {
    private var mCount: Int = 1
    private val mContext = context
    private val mDateTimeFormatter = dateTimeFormatter

    /**
     * It saves (i.e. writes) the current counter value to the counter.txt file in the device's internal memory, along with the date-time information on which it was recorded, and returns true if the operation is successful.
     */
    fun saveCurrentSecond(seconds: Long): Boolean {
        BufferedWriter(mContext.openFileOutput(FILE_NAME, Context.MODE_APPEND)
            .writer(StandardCharsets.UTF_8))
            .use {
                val nowStr = mDateTimeFormatter.format(LocalDateTime.now())
                it.write("${"%02d".format(mCount++)}. ${"%02d".format(seconds)} ($nowStr)\r\n")
            }

        return true
    }

    fun removeAllSavedSecondsFromFile() {
        mCount = 1

        // This truncates the file (i.e. deletes all of its contents).
        // Dosyayı private mode'da açmak, dosyayı sıfırlamak demektir.
        mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {  }
    }
}