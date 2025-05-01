package com.edaakyil.android.kotlin.app.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.edaakyil.android.kotlin.app.sample.databinding.ActivityPaymentBinding
import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment
import com.edaakyil.android.kotlin.app.sample.payment.constant.DRINK_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.constant.FOOD_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.constant.MENU_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.constant.MENU_PAYMENT2
import com.edaakyil.android.kotlin.app.sample.payment.product.Fruit
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPaymentBinding

    @Inject
    lateinit var fruit: Fruit

    @Inject
    @Named(FOOD_PAYMENT)
    lateinit var foodPayment: IProductPayment

    @Inject
    @Named(DRINK_PAYMENT)
    lateinit var drinkPayment: IProductPayment

    @Inject
    @Named(MENU_PAYMENT)
    lateinit var menuPayment: IProductPayment

    @Inject
    @Named(MENU_PAYMENT2)
    lateinit var menuPayment2: IProductPayment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()

        Toast.makeText(this, "PaymentActivity: Fruit -> ${fruit.calculatePayment(8.0)}", Toast.LENGTH_SHORT).show()

        Toast.makeText(this, "PaymentActivity: FoodPayment -> ${foodPayment.calculatePayment(5.0)}", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "PaymentActivity: DrinkPayment -> ${drinkPayment.calculatePayment(3.0)}", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "PaymentActivity: MenuPayment -> ${menuPayment.calculatePayment(1.0)}", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "PaymentActivity: MenuPayment2 -> ${menuPayment2.calculatePayment(1.0)}", Toast.LENGTH_SHORT).show()

        //Toast.makeText(this, "PaymentActivity: MenuPayment -> ${menuPayment.deneme()}", Toast.LENGTH_LONG).show()
        //Toast.makeText(this, "PaymentActivity: FoodPayment -> ${foodPayment.deneme()}", Toast.LENGTH_LONG).show()
        //Toast.makeText(this, "PaymentActivity: DrinkPayment -> ${drinkPayment.deneme()}", Toast.LENGTH_LONG).show()
    }

    private fun initialize() {
        enableEdgeToEdge()
        initBinding()
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.paymentActivityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        initModels()
    }

    private fun initModels() {

    }
}