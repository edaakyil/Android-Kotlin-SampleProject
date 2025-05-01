package com.edaakyil.android.kotlin.app.sample.payment.product

import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment

abstract class BaseProduct(var category: String, var name: String, var unitPrice: Double) : IProductPayment
