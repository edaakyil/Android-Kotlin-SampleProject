package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.constant.DRINK
import com.edaakyil.android.kotlin.app.sample.payment.product.BaseProduct
import com.edaakyil.android.kotlin.app.sample.payment.product.Drink
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
internal abstract class DrinkModule {
    @Binds
    @Named(DRINK)
    internal abstract fun bindDrink(drink: Drink): BaseProduct
}