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
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityPostalCodeBinding
import com.edaakyil.android.kotlin.app.sample.api.geonames.postalcodesearch.service.IPostalCodeService
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class PostalCodeActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPostalCodeBinding

    @Inject
    lateinit var postalCodeService: IPostalCodeService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.postalCodeActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val call = postalCodeService.findByPostalCode("csystem", "tr", "34843")
        call.enqueue(postalCodeCallback())
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_postal_code)
        initModels()
    }

    private fun initModels() {
        mBinding.activity = this
    }

    private fun postalCodeCallback(): Callback<PostalCodes> {
        return object : Callback<PostalCodes> {
            override fun onResponse(call: Call<PostalCodes?>, response: Response<PostalCodes?>) {
                if (response.code() != 200) {
                    Log.e("Status", response.code().toString())
                    Toast.makeText(this@PostalCodeActivity, "Unsuccessful operation", Toast.LENGTH_LONG).show()
                    return
                }

                Log.i("Response:", response.raw().toString())
                Toast.makeText(this@PostalCodeActivity, response.raw().toString(), Toast.LENGTH_LONG).show()

                response.body()!!.postalCodes.forEach { Toast.makeText(this@PostalCodeActivity, it.placeName, Toast.LENGTH_LONG).show() }
            }

            override fun onFailure(call: Call<PostalCodes?>, ex: Throwable) {
                Log.e("onFailure", ex.message.toString())
                Toast.makeText(this@PostalCodeActivity, "Error occurred while connection", Toast.LENGTH_LONG).show()
            }
        }
    }
}