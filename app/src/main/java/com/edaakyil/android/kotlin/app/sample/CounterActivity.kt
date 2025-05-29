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

private const val SECONDS_LIMIT = 20

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

        counterDataService.setLimit(SECONDS_LIMIT)
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_counter)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
        mBinding.dateTimeText = ""
        mBinding.startStopButtonText = getString(R.string.start)
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

    private fun setCounterTextWithDataBinding(hour: Long, minute: Long, second: Long) {
        mBinding.counterText = getString(R.string.counter_text).format(hour, minute, second)
    }

    private fun setCounterTextWithViewBinding(hour: Long, minute: Long, second: Long) {
        runOnUiThread { "%02d:%02d:%02d".format(hour, minute, second).also { mBinding.counterActivityTextViewCounter.text = it } }
    }

    private fun schedulerCallback() {
        ++mSeconds

        val hour = mSeconds / 60 / 60
        val minute = mSeconds / 60 % 60
        val second = mSeconds % 60

        setCounterTextWithDataBinding(hour, minute, second)
        setCounterTextWithViewBinding(hour, minute, second)
    }

    private fun showAlertDialogForResetButton() {
        AlertDialog.Builder(this)
            .setTitle(R.string.alert_title)
            .setMessage(R.string.reset_counter_error_message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .show()
    }

    // UI şlemlerinin UI thread'de yani maini/primary thread'de yapılması gerekir ve
    // Reset butonundaki akış başka bir thread'de olduğu için runOnUiThread'i kullandık
    private fun resetButtonCallback() {
        if (!counterDataService.saveCurrentSecond(mSeconds)) {
            runOnUiThread { showAlertDialogForResetButton() }
            return
        }

        mSeconds = 0L
        mBinding.counterText = getString(R.string.counter_text).format(0, 0, 0)
        runOnUiThread { mBinding.counterActivityTextViewCounter.text = getString(R.string.counter_text).format(0, 0, 0) }
    }

    private fun loadAllSecondsThreadCallback() {
        try {
            val seconds = counterDataService.findAllSavedSeconds()

            runOnUiThread { mBinding.adapter?.clear(); mBinding.adapter?.addAll(seconds) }
        } catch (ex: DataServiceException) {
            runOnUiThread { Toast.makeText(this, R.string.io_problem_message, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun loadSecondThreadCallback() {
        try {
            TODO()
        } catch (ex: DataServiceException) {
            runOnUiThread { Toast.makeText(this, R.string.io_problem_message, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    /**
     * When CounterActivity is destroyed, the counter and date-time timers will be stopped.
     */
    override fun onDestroy() {
        super.onDestroy()

        if (mCounterScheduledFuture != null)
            mCounterScheduledFuture?.cancel(false)

        mDateTimeScheduledFuture.cancel(false)
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
            .setPositiveButton(R.string.yes) { _, _ -> threadPool.execute { counterDataService.removeAllSavedSecondsFromFile() }; mBinding.adapter?.clear() }
            .setNegativeButton(R.string.no) { _, _ -> }
            //.create()
            .show()
    }

    fun onLoadAllButtonClicked() {
        threadPool.execute { loadAllSecondsThreadCallback() }
        mBinding.counterActivityLoadButton.visibility = View.VISIBLE
    }

    /**
     * Load selected second from list of seconds be loaded when Load All button clicked
     */
    fun onLoadButtonClicked() {
        val position = mBinding.counterActivityListViewSeconds.checkedItemPosition

        if (position != -1)
            threadPool.execute { loadSecondThreadCallback() }
    }
}