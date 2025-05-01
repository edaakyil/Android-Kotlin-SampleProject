package com.edaakyil.android.kotlin.app.sample.payment.product

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import org.csystem.kotlin.util.string.randomTextEN
import javax.inject.Inject
import kotlin.random.Random

@ActivityScoped
class Dessert @Inject constructor(@ActivityContext var context: Context) : BaseProduct("Dessert", Random.randomTextEN(4), Random.nextDouble(1.0, 100.0)) {
    override fun calculatePayment(amount: Double): Double {
        Toast.makeText(context, "Category: $category\nName: $name\nAmount: $amount\nUnitPrice: $unitPrice", Toast.LENGTH_SHORT).show()

        return amount * unitPrice
    }

    override fun deneme(): String {
        Toast.makeText(context, "$category Deneme", Toast.LENGTH_LONG).show()

        return "$category Deneme"
    }
}