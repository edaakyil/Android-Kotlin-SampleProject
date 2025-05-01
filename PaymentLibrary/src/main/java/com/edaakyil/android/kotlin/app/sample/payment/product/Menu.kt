package com.edaakyil.android.kotlin.app.sample.payment.product

import android.content.Context
import android.widget.Toast
import com.edaakyil.android.kotlin.app.sample.payment.constant.DESSERT
import com.edaakyil.android.kotlin.app.sample.payment.constant.DRINK
import com.edaakyil.android.kotlin.app.sample.payment.constant.FOOD
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

@ActivityScoped
class Menu @Inject constructor(
    @ApplicationContext var context: Context,
    @Named(FOOD) var food: BaseProduct,
    @Named(DRINK) var drink: BaseProduct,
    @Named(DESSERT) var dessert: BaseProduct,
    private var fruit: Fruit
): BaseProduct("Menu", "", Random.nextDouble(1.0, 100.0)) {
    override fun calculatePayment(amount: Double): Double {
        Toast.makeText(context, "$category created", Toast.LENGTH_SHORT).show()

        return amount * (food.calculatePayment(1.0) + drink.calculatePayment(1.0) + dessert.calculatePayment(1.0) + fruit.calculatePayment(3.0))
    }

    override fun deneme(): String {
        Toast.makeText(context, "$category Deneme", Toast.LENGTH_SHORT).show()

        return "$category Deneme"
    }
}