package com.edaakyil.android.kotlin.app.sample.payment.product

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.random.Random

@ActivityScoped
class Menu2 @Inject constructor(
    @ApplicationContext var context: Context,
    private var food: Food,
    private var drink: Drink,
    private var dessert: Dessert
): BaseProduct("Menu-2", "", Random.nextDouble(1.0, 100.0)) {
    override fun calculatePayment(amount: Double): Double {
        Toast.makeText(context, "$category created", Toast.LENGTH_SHORT).show()

        return amount * (food.calculatePayment(1.0) + drink.calculatePayment(2.0) + dessert.calculatePayment(3.0))
    }

    override fun deneme(): String {
        Toast.makeText(context, "$category Deneme", Toast.LENGTH_SHORT).show()

        return "$category Deneme"
    }
}