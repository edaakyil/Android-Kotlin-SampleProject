package com.edaakyil.android.kotlin.app.sample

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.dto.RandomUser
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityRandomUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedInputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@AndroidEntryPoint
class RandomUserDetailsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRandomUserDetailsBinding

    @Inject
    lateinit var threadPool: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.randomUserDetailsActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_random_user_details)
        initUI()
    }

    private fun initUI() {
        val randomUser = intent.getSerializableExtra("RANDOM_USER") as RandomUser

        setAndDownloadPicture(randomUser.picture.large)
    }

    private fun setAndDownloadPicture(urlStr: String) {
        threadPool.execute { loadPictureCallback(urlStr) }
    }

    private fun loadPictureCallback(urlStr: String) {
        try {
            val url = URL(urlStr)
            val connection = url.openConnection()

            connection.connect()

            val inputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            var bitmap = BitmapFactory.decodeStream(bufferedInputStream)

            runOnUiThread { mBinding.randomUserDetailsActivityUserPicture.setImageBitmap(bitmap) }
        } catch (ex: MalformedURLException) {
            Log.e("url-error", ex.message!!)
        } catch (ex: IOException) {
            Log.e("io-error", ex.message!!)
            runOnUiThread { finish() }
        }
    }
}