package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.dto.PostalCodes
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityPostalCodesBinding
import com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.service.IPostalCodeService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

@AndroidEntryPoint
class PostalCodesActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPostalCodesBinding

    @Inject
    lateinit var postalCodeService: IPostalCodeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.postalCodesActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val call = postalCodeService.findByPostalCode("csystem", "tr", "34843")
        call.enqueue(postalCodeCallback())
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_postal_codes)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
    }

    private fun postalCodeCallback(): Callback<PostalCodes> {
        return object : Callback<PostalCodes> {
            override fun onResponse(call: Call<PostalCodes?>, response: Response<PostalCodes?>) {
                Log.i("Response-Raw", response.raw().toString())

                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Log.e("Status", response.code().toString())
                    Toast.makeText(this@PostalCodesActivity, "Unsuccessful operation", Toast.LENGTH_LONG).show()
                    return
                }

                if (response.body()?.postalCodes == null) {
                    Toast.makeText(this@PostalCodesActivity, "Limit exhausted", Toast.LENGTH_LONG).show()
                    return
                }

                response.body()!!.postalCodes.forEach { Toast.makeText(this@PostalCodesActivity, it.placeName, Toast.LENGTH_LONG).show() }
            }

            override fun onFailure(call: Call<PostalCodes?>, ex: Throwable) {
                Log.e("onFailure", ex.message.toString())
                Toast.makeText(this@PostalCodesActivity, "Error occurred while connection", Toast.LENGTH_LONG).show()
            }
        }
    }
}