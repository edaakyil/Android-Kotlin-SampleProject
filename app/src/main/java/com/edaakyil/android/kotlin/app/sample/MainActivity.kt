package com.edaakyil.android.kotlin.app.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityMainBinding
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.CurrentLocalDateTimeInterceptor
import com.edaakyil.android.kotlin.lib.util.datetime.module.annotation.DateTimeFormatterTRInterceptor
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var dateTime: LocalDateTime

    @Inject
    lateinit var dateTimeFormatter: DateTimeFormatter

    @Inject
    @CurrentLocalDateTimeInterceptor
    lateinit var dateTimeEdaLib: LocalDateTime

    @Inject
    @DateTimeFormatterTRInterceptor
    lateinit var dateTimeFormatterEdaLib: DateTimeFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.mainActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
        mBinding.dateTime = dateTimeFormatter.format(dateTime)
        mBinding.dateTimeEdaLib = resources.getString(R.string.eda_date_time_text, dateTimeFormatterEdaLib.format(dateTimeEdaLib))
        //mBinding.dateTimeEdaLib = resources.getString(R.string.date_time_text, "").format(dateTimeFormatterEdaLib.format(dateTimeEdaLib))
    }

    fun onCounterPageButtonClicked() {
        Intent(this, CounterActivity::class.java).apply { startActivity(this) }
    }

    fun onPaymentPageButtonClicked() {
        Intent(this, PaymentActivity::class.java).apply { startActivity(this) }
    }

    fun onPostalCodesPageButtonClicked() {
        Intent(this, PostalCodesActivity::class.java).apply{ startActivity(this) }
    }

    fun onRandomUsersPageButtonClicked() {
        Intent(this, RandomUsersActivity::class.java).apply{ startActivity(this) }
    }
}