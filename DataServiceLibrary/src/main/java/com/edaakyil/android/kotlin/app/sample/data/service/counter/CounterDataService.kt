package com.edaakyil.android.kotlin.app.sample.data.service.counter

import android.content.Context
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.DateTimeFormatterENInterceptor
import com.karandev.data.exception.service.DataServiceException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

private const val SECONDS_LIMIT = 20
private const val FILE_NAME = "counter.txt"

@Singleton
class CounterDataService @Inject constructor(
    @ApplicationContext context: Context,
    @DateTimeFormatterENInterceptor dateTimeFormatter: DateTimeFormatter
) {
    private val mContext = context
    private val mDateTimeFormatter = dateTimeFormatter
    private val mFile = File(mContext.filesDir, FILE_NAME)
    var count: Int = if (!mFile.exists()) 0 else countOfSavedSeconds()
    var limit: Int = SECONDS_LIMIT
        set(value) {
            // Here default limit (-1) means limitless
            require(value > 0 || value == -1) { "Invalid limit value" }

            field = value
        }

    private fun countOfSavedSeconds(): Int {
        try {
            BufferedReader(mContext.openFileInput(FILE_NAME).reader(StandardCharsets.UTF_8)). use {
                //return it.readLines().size

                // line sayısı çok fazla olursa line-line okumak yorabileceği için readLine işlemini generateSequence ile yaptık yani döngü ile okuduk
                // Bu şekilde arkaplanda stream bunu daha basit hale getiricek
                return generateSequence { it.readLine() }.takeWhile { it != null }.count()
            }
        } catch (ex: IOException) {
            throw DataServiceException("CounterDataService.countData", ex)
        }
    }

    fun saveCurrentSecond(second: Long) {
        try {
            BufferedWriter(mContext.openFileOutput(FILE_NAME, Context.MODE_APPEND).writer(StandardCharsets.UTF_8))
                .use {
                    val nowStr = mDateTimeFormatter.format(LocalDateTime.now())
                    it.write("${"%02d".format(++count)}@${"%02d".format(second)}@($nowStr)\r\n") // it.write("$seconds@$nowStr\n")
                }
        } catch (ex: IOException) {
            throw DataServiceException("CounterDataService.saveCurrentSecond.save", ex)
        }
    }

    /**
     * It saves (i.e. writes) the current counter value to the counter.txt file in the device's internal memory, along with the date-time information on which it was recorded, and returns true if the operation is successful.
     */
    fun saveCurrentSecondByLimit(second: Long): Boolean {
        var result = true

        try {
            if (limit == -1 || countOfSavedSeconds() < limit) {
                saveCurrentSecond(second)
                result = true
            } else {
                result = false
            }
        } catch (_: FileNotFoundException) {
            saveCurrentSecond(second)
        } catch (ex: IOException) {
            throw DataServiceException("CounterDataService.saveSeconds", ex)
        }

        return result
    }

    fun removeAllSavedSecondsFromFile() {
        count = 0

        try {
            // This truncates the file (i.e. deletes all of its contents).
            // Dosyayı private mode'da açmak, dosyayı sıfırlamak demektir.
            mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {  }
        } catch (ex: IOException) {
            throw DataServiceException("CounterDataService.removeAll", ex)
        }
    }

    fun findAllSavedSeconds(): List<String> {
        try {
            BufferedReader(mContext.openFileInput(FILE_NAME).reader(StandardCharsets.UTF_8)).use {
                //return it.readLines()
                return generateSequence { it.readLine() }.takeWhile { it != null }.toList()
            }
        } catch (ex: IOException) {
            throw DataServiceException("CounterDataService.findAll", ex)
        }
    }
}