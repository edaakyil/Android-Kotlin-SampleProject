package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment
import com.edaakyil.android.kotlin.app.sample.payment.constant.FOOD_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.product.Food
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
abstract class FoodPaymentModule {
    @Binds
    @Named(FOOD_PAYMENT)
    abstract fun bindFoodPayment(food: Food): IProductPayment
}