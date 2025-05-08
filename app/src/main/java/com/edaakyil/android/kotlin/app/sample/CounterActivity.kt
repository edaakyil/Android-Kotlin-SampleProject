package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityCounterBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CounterActivity : AppCompatActivity() {
    private var mSeconds = 0L
    private var mStartedFlag = false
    private var mScheduledFuture: ScheduledFuture<*>? = null
    private lateinit var mBinding: ActivityCounterBinding

    @Inject
    @Named("counterActivityScheduledExecutorService")
    lateinit var scheduledThreadPool: ScheduledExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
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
        mBinding.counterText = getString(R.string.counter_text).format(0, 0, 0)
        mBinding.startStopButtonText = getString(R.string.start)
    }

    private fun setCounterText() {
        val hour = mSeconds / 60 / 60
        val minute = mSeconds / 60 % 60
        val second = mSeconds % 60

        mBinding.counterText = getString(R.string.counter_text).format(hour, minute, second)
    }

    private fun schedulerCallback() {
        setCounterText()
        ++mSeconds
    }

    fun onStartStopButtonClicked() {
        if (mStartedFlag) {
            mBinding.startStopButtonText = getString(R.string.start)
            mScheduledFuture?.cancel(false)
        } else {
            mBinding.startStopButtonText = getString(R.string.stop)
            mScheduledFuture = scheduledThreadPool.scheduleWithFixedDelay({ schedulerCallback() }, 0, 1, TimeUnit.SECONDS)
        }

        mStartedFlag = !mStartedFlag
    }
}