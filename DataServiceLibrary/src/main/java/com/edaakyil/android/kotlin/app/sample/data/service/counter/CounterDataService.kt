package com.edaakyil.android.kotlin.app.sample.data.service.counter

import android.content.Context
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.DateTimeFormatterENInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE_NAME = "counter.txt"

@Singleton
class CounterDataService @Inject constructor(
    @ApplicationContext context: Context,
    @DateTimeFormatterENInterceptor dateTimeFormatter: DateTimeFormatter
) {
    private val mContext = context
    private val mDateTimeFormatter = dateTimeFormatter
    private val mFile = File(mContext.filesDir, FILE_NAME)
    private var mCount: Int = setCount()
    private var mLimit: Int = -1

    private fun setCount() = if (!mFile.exists()) 1 else countOfSavedSeconds() + 1

    private fun countOfSavedSeconds(): Int {
        BufferedReader(mContext.openFileInput(FILE_NAME).reader(StandardCharsets.UTF_8)). use {
            return it.readLines().size
        }
    }

    fun setLimit(limit: Int = -1) {
        // Here default limit (-1) means limitless
        if (limit <= 0 && limit != -1)
            throw IllegalArgumentException("Invalid limit value.")

        mLimit = limit
    }

    /**
     * It saves (i.e. writes) the current counter value to the counter.txt file in the device's internal memory, along with the date-time information on which it was recorded, and returns true if the operation is successful.
     */
    fun saveCurrentSecond(seconds: Long): Boolean {
        fun save() {
            BufferedWriter(mContext.openFileOutput(FILE_NAME, Context.MODE_APPEND).writer(StandardCharsets.UTF_8))
                .use {
                    val nowStr = mDateTimeFormatter.format(LocalDateTime.now())
                    it.write("${"%02d".format(mCount++)}. ${"%02d".format(seconds)} ($nowStr)\r\n")
                }
        }

        var result = true

        try {
            if (mLimit == -1 || countOfSavedSeconds() < mLimit) {
                mCount = setCount()
                save()
                result = true
            } else {
                result = false
            }
        } catch (_: FileNotFoundException) {
            save()
        }

        return result
    }

    fun removeAllSavedSecondsFromFile() {
        mCount = 1

        // This truncates the file (i.e. deletes all of its contents).
        // Dosyayı private mode'da açmak, dosyayı sıfırlamak demektir.
        mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {  }
    }

    fun findAllSavedSeconds(): List<String> {
        BufferedReader(mContext.openFileInput(FILE_NAME).reader(StandardCharsets.UTF_8)).use {
            //return it.readLines()
            return generateSequence { it.readLine() }.takeWhile { it != null }.toList()
        }
    }
}