package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.constant.FOOD
import com.edaakyil.android.kotlin.app.sample.payment.product.BaseProduct
import com.edaakyil.android.kotlin.app.sample.payment.product.Food
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
internal abstract class FoodModule {
    @Binds
    @Named(FOOD)
    internal abstract fun bindFood(food: Food): BaseProduct
}