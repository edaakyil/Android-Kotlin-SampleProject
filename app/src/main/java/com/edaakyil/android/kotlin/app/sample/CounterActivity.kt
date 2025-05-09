package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityCounterBinding
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.DateTimeFormatterENInterceptor
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    @DateTimeFormatterENInterceptor
    lateinit var dateTimeFormatterEN: DateTimeFormatter

    @Inject
    @Named("counterActivityScheduledExecutorService")
    lateinit var counterScheduledThreadPool: ScheduledExecutorService

    @Inject
    @Named("counterActivityScheduledExecutorService")
    lateinit var dateTimeScheduledThreadPool: ScheduledExecutorService

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
        mBinding.counterText = getString(R.string.counter_text).format(0, 0, 0)
        mBinding.counterActivityTextViewCounter.text = getString(R.string.counter_text).format(0, 0, 0)
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
        val hour = mSeconds / 60 / 60
        val minute = mSeconds / 60 % 60
        val second = mSeconds % 60

        setCounterTextWithDataBinding(hour, minute, second)
        setCounterTextWithViewBinding(hour, minute, second)

        ++mSeconds
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
     * Each time the Reset button is clicked, the counter will be reset.
     */
    fun onResetButtonClicked() {
        mSeconds = 0L
        mBinding.counterText = getString(R.string.counter_text).format(0, 0, 0)
        mBinding.counterActivityTextViewCounter.text = getString(R.string.counter_text).format(0, 0, 0)
    }
}