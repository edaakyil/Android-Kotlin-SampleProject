package com.edaakyil.android.kotlin.app.sample.payment

interface IProductPayment {
    fun calculatePayment(amount: Double): Double

    fun deneme(): String
}