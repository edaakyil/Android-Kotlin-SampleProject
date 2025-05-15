package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.limitConfigurationActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_limit_configuration)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    fun onSaveButtonClicked() {
        try {
            counterDataService.setLimit(mBinding.limitValue!!.toInt())
            finish()
        } catch (_: NumberFormatException) {
            Toast.makeText(this, R.string.invalid_value_message, Toast.LENGTH_SHORT).show()
        }
    }

    fun onCloseButtonClicked() =  finish()
}