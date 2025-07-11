package com.edaakyil.android.kotlin.app.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.data.service.counter.CounterDataService
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityCounterBinding
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.DateTimeFormatterENInterceptor
import com.karandev.data.exception.service.DataServiceException
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CounterActivity : AppCompatActivity() {
    private var mSeconds = 0L
    private var mStartedFlag = false
    private var mCounterScheduledFuture: ScheduledFuture<*>? = null
    private lateinit var mDateTimeScheduledFuture: ScheduledFuture<*>
    private lateinit var mBinding: ActivityCounterBinding

    @Inject
    lateinit var counterDataService: CounterDataService

    @Inject
    @DateTimeFormatterENInterceptor
    lateinit var dateTimeFormatterEN: DateTimeFormatter

    @Inject
    @Named("singleThreadExecutorService")
    lateinit var threadPool: ExecutorService

    @Inject
    @Named("scheduledExecutorService")
    lateinit var counterScheduledThreadPool: ScheduledExecutorService

    @Inject
    @Named("scheduledExecutorService")
    lateinit var dateTimeScheduledThreadPool: ScheduledExecutorService

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.counterActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_counter)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
        mBinding.dateTimeText = ""
        mBinding.startStopButtonText = getString(R.string.start)
        mBinding.limit = counterDataService.limit.toString()
        mBinding.count = counterDataService.count.toString()
        mBinding.counterText = getString(R.string.counter_text).format(0, 0, 0)
        mBinding.counterActivityTextViewCounter.text = mBinding.counterText
        mBinding.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, mutableListOf<String>())
        startDateTimeScheduler()
    }

    private fun dateTimeSchedulerCallback() {
        val now = LocalDateTime.now()

        mBinding.dateTimeText = dateTimeFormatterEN.format(now)
    }

    private fun startDateTimeScheduler() {
        mDateTimeScheduledFuture = dateTimeScheduledThreadPool.scheduleWithFixedDelay({ dateTimeSchedulerCallback() }, 0L, 1L, TimeUnit.SECONDS)
    }

    private fun setCounterTexts(flag: Boolean = true) {
        val hour = mSeconds / 60 / 60
        val minute = mSeconds / 60 % 60
        val second = mSeconds % 60

        mBinding.counterText = getString(R.string.counter_text).format(hour, minute, second)

        if (flag)
            runOnUiThread { "%02d:%02d:%02d".format(hour, minute, second).also { mBinding.counterActivityTextViewCounter.text = it } }
        else
            "%02d:%02d:%02d".format(hour, minute, second).also { mBinding.counterActivityTextViewCounter.text = it }

    }

    private fun schedulerCallback() {
        ++mSeconds
        setCounterTexts()
    }

    private fun showAlertDialogForResetButton() {
        AlertDialog.Builder(this)
            .setTitle(R.string.alert_title)
            .setMessage(R.string.counter_activity_reset_button_exceeding_limit_message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .show()
    }

    // UI şlemlerinin UI thread'de yani maini/primary thread'de yapılması gerekir ve
    // Reset butonundaki akış başka bir thread'de olduğu için runOnUiThread'i kullandık
    private fun resetButtonCallback() {
        if (!counterDataService.saveCurrentSecondByLimit(mSeconds)) {
            runOnUiThread { showAlertDialogForResetButton() }
            return
        }

        runOnUiThread {
            mBinding.adapter?.addAll(counterDataService.findAllSavedSeconds()[counterDataService.count - 1])
            mBinding.counterActivityTextViewCounter.text = getString(R.string.counter_text).format(0, 0, 0)
        }

        mBinding.count = counterDataService.count.toString()
        mBinding.counterText = getString(R.string.counter_text).format(0, 0, 0)
        mSeconds = 0L
    }

    private fun removeAllSecondsCallback() {
        threadPool.execute { counterDataService.removeAllSavedSecondsFromFile(); mBinding.count = counterDataService.count.toString() }
        mBinding.adapter?.clear()
    }

    private fun loadAllSecondsThreadCallback() {
        try {
            val seconds = counterDataService.findAllSavedSeconds()

            runOnUiThread { mBinding.adapter?.clear(); mBinding.adapter?.addAll(seconds) }
        } catch (ex: DataServiceException) {
            runOnUiThread { Toast.makeText(this, R.string.io_problem_message, Toast.LENGTH_SHORT).show() }
        }
    }

    /*
    Limit var
            mSecond = second
            saveSecond()
            setText()

    Limit yok
        Yes
            mSecond = second
            setText()
        No

     */

    private fun loadSecond(second: Long, flag: Boolean) {
        mSeconds = second
        setCounterTexts(flag)
        mBinding.count = counterDataService.count.toString()
    }

    private fun saveAndLoadSecond(second: Long, flag: Boolean) {
        counterDataService.saveCurrentSecond(mSeconds)
        loadSecond(second, flag)
        runOnUiThread { mBinding.adapter?.addAll(counterDataService.findAllSavedSeconds()[counterDataService.count - 1]) }
    }

    /**
     * If limit is inadequate, AlertDialog will be shown for loading without saving
     */
    private fun showAlertDialogForLimit(second: Long) {
        AlertDialog.Builder(this)
            .setTitle(R.string.alert_title)
            .setMessage(R.string.counter_activity_load_button_exceeding_limit_message)
            .setPositiveButton(R.string.counter_activity_load_button_exceeding_limit_positive_button) { _, _ ->  saveAndLoadSecond(second, false) }
            .setNegativeButton(R.string.counter_activity_load_button_exceeding_limit_negative_button) { _, _ -> loadSecond(second, false) }
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun getSecondByRecord(str: String): Long {
        // data'yı pars etme
        val info = str.split('@')

        return info[1].toLong()
    }

    private fun loadSecondThreadCallback(position: Int) {
        try {
            val second = getSecondByRecord(mBinding.adapter!!.getItem(position)!!)

            if (counterDataService.limit <= counterDataService.count && counterDataService.limit != -1)
                runOnUiThread { showAlertDialogForLimit(second) }
            else
                saveAndLoadSecond(second, true)

        } catch (ex: DataServiceException) {
            runOnUiThread { Toast.makeText(this, R.string.io_problem_message, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    override fun onResume() {
        super.onResume()

        startDateTimeScheduler()

        if (mStartedFlag)
            mCounterScheduledFuture = counterScheduledThreadPool.scheduleWithFixedDelay({ schedulerCallback() }, 0, 1, TimeUnit.SECONDS)

        mBinding.limit = if (counterDataService.limit == -1) "Limitless" else counterDataService.limit.toString()
    }

    /**
     * When CounterActivity is destroyed, the counter and date-time timers will be stopped.
     */
    override fun onPause() {
        super.onPause()

        mDateTimeScheduledFuture.cancel(false)

        if (mCounterScheduledFuture != null)
            mCounterScheduledFuture?.cancel(false)
    }

    fun onStartStopButtonClicked() {
        if (mStartedFlag) {
            mBinding.startStopButtonText = getString(R.string.start)
            mCounterScheduledFuture?.cancel(false)
            mCounterScheduledFuture = null
        } else {
            mBinding.startStopButtonText = getString(R.string.stop)
            mCounterScheduledFuture = counterScheduledThreadPool.scheduleWithFixedDelay({ schedulerCallback() }, 0, 1, TimeUnit.SECONDS)
        }

        mStartedFlag = !mStartedFlag
    }

    /**
     * Each time the Reset button is clicked, the current counter value will be saved (i.e. written) to the counter.txt file in the device's internal memory and the counter will be reset.
     */
    fun onResetButtonClicked() {
        // Reset butonunda yapılan işlemleri CounterActivity tarafında asenkron hale getirildi.
        // Herbir akış kendi içerisinde olduğundan thread sayısını arttırmamıza gerek yok
        threadPool.execute { resetButtonCallback() }
    }

    fun onConfigureButtonClicked() {
        Intent(this, LimitConfigurationActivity::class.java).apply { startActivity(this) }
    }

    /**
     * Each time the Reset All button is clicked, the counter values recorded in the internal memory of the device will be deleted from this file.
     */
    fun onRemoveAllButtonClicked() {
        AlertDialog.Builder(this)
            .setTitle(R.string.remove_all_counter_title)
            .setMessage(R.string.remove_all_counter_message) // Are you sure you want to delete the all saved counters from the file?
            .setPositiveButton(R.string.yes) { _, _ -> removeAllSecondsCallback() }
            .setNegativeButton(R.string.no) { _, _ -> }
            //.create()
            .show()
    }

    fun onLoadAllButtonClicked() {
        threadPool.execute { loadAllSecondsThreadCallback() }
        mBinding.counterActivityLoadButton.visibility = View.VISIBLE
    }

    /**
     * Loads selected second from list of seconds be loaded when Load All button clicked and Saves the current second on the screen
     */
    fun onLoadButtonClicked() {
        val position = mBinding.counterActivityListViewSeconds.checkedItemPosition

        if (position != -1)
            threadPool.execute { loadSecondThreadCallback(position) }
    }
}


