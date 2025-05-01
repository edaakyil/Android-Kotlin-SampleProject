package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment
import com.edaakyil.android.kotlin.app.sample.payment.constant.DRINK_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.product.Drink
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
abstract class DrinkPaymentModule {
    @Binds
    @Named(DRINK_PAYMENT)
    abstract fun bindDrinkPayment(drink: Drink): IProductPayment
}