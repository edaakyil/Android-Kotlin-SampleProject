package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.api.constant.STATUS_OK
import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.dto.RandomUser
import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.dto.RandomUserInfo
import com.edaakyil.android.kotlin.app.sample.api.randomuser.me.service.IRandomUserService
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityRandomUserBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val DEFAULT_USER_COUNT = 10

@AndroidEntryPoint
class RandomUserActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRandomUserBinding

    @Inject
    lateinit var randomUserService: IRandomUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.randomUserActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_random_user)
        initModels()
    }

    private fun initModels() {
        //mBinding.count = ""
        mBinding.activity = this
        mBinding.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList<RandomUser>())
        //mBinding.adapter = ArrayAdapter<RandomUser>(this, android.R.layout.simple_list_item_1, ArrayList())
    }

    private fun findRandomUserCallback(): Callback<RandomUserInfo> {
        return object : Callback<RandomUserInfo> {
            override fun onResponse(call: Call<RandomUserInfo?>, response: Response<RandomUserInfo?>) {
                Log.i("Response-Raw", response.raw().toString())

                if (response.code() != STATUS_OK) {
                    Log.e("Status", response.code().toString())
                    Toast.makeText(this@RandomUserActivity, "Unsuccessful operation", Toast.LENGTH_LONG).show()
                    return
                }

                if (response.body()?.users == null) {
                    Toast.makeText(this@RandomUserActivity, "Limit exhausted", Toast.LENGTH_LONG).show()
                    return
                }

                response.body()!!.users.forEach { mBinding.adapter!!.add(it) }
            }

            override fun onFailure(call: Call<RandomUserInfo?>, ex: Throwable) {
                Log.e("onFailure", ex.message.toString())
                Toast.makeText(this@RandomUserActivity, "Error occurred while connection", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onGetUsersButtonClicked() {
        try {
            var count = DEFAULT_USER_COUNT
            val countStr = mBinding.count!!.trim()

            mBinding.count = ""
            mBinding.adapter?.clear()

            if (countStr.isNotBlank())
                count = countStr.toInt()

            if (count <= 0) {
                Toast.makeText(this, R.string.value_must_be_positive_prompt, Toast.LENGTH_SHORT).show()
                throw NumberFormatException("Invalid value")
            }

            (1..count).forEach {
                randomUserService.findUser().enqueue(findRandomUserCallback())
            }
        } catch (ex: NumberFormatException) {
            Log.e("NumberFormatException", ex.message!!)
            Toast.makeText(this, R.string.message_positive, Toast.LENGTH_LONG).show()
        }
    }

    fun onUserSelected(position: Int) {
        val user = mBinding.adapter!!.getItem(position)

        TODO()
    }
}



/*
 <data>
        <import type="com.edaakyil.android.kotlin.app.sample.api.randomuser.me.dto.RandomUserInfo"/>
        <import type="com.edaakyil.android.kotlin.app.sample.RandomUserActivity"/>
        <import type="android.widget.ArrayAdapter"/>

        <variable name="adapter" type="ArrayAdapter&lt;RandomUserInfo&gt;"/>
        <variable name="activity" type="RandomUserActivity"/>
        <variable name="count" type="String"/>
    </data>

<ListView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:adapter="@{adapter}"
            android:onItemClick="@{ (p0, p1, pos, p4) -> activity.onUserSelected(pos) }"/>

 */