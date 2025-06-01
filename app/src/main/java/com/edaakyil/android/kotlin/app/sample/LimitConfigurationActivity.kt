package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.data.service.counter.CounterDataService
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityLimitConfigurationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LimitConfigurationActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLimitConfigurationBinding

    @Inject
    lateinit var counterDataService: CounterDataService

    @Inject
    @Named("singleThreadExecutorService")
    lateinit var threadPool: ExecutorService

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_limit_configuration)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
        mBinding.limitValue = if (counterDataService.limit == -1) "Limitless" else counterDataService.limit.toString()
        mBinding.flag = counterDataService.limit != -1
    }

    private fun saveButtonCallback(limit: Int) {
        try {
            counterDataService.limit = limit
            finish()
        } catch (_: NumberFormatException) {
            runOnUiThread { Toast.makeText(this, R.string.invalid_value_message, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    fun onEditTextClicked() {
        mBinding.limitValue = ""
    }

    fun onIncreaseButtonClicked() {
        mBinding.limitValue = (++counterDataService.limit).toString()
    }

    fun onDecreaseButtonClicked() {
        mBinding.limitValue = (--counterDataService.limit).toString()
    }

    fun onSaveButtonClicked() = threadPool.execute { saveButtonCallback(mBinding.limitValue!!.toInt()) }

    fun onNoLimitButtonClicked() = threadPool.execute { saveButtonCallback(-1) }


    fun onCloseButtonClicked() =  finish()
}