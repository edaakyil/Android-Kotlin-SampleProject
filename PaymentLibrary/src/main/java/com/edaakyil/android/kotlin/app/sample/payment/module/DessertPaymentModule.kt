package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment
import com.edaakyil.android.kotlin.app.sample.payment.constant.DESSERT_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.product.Dessert
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
abstract class DessertPaymentModule {
    @Binds
    @Named(DESSERT_PAYMENT)
    abstract fun bindDessertPayment(dessert: Dessert): IProductPayment
}