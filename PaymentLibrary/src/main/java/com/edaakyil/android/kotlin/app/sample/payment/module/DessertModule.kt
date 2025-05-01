package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.constant.DESSERT
import com.edaakyil.android.kotlin.app.sample.payment.product.BaseProduct
import com.edaakyil.android.kotlin.app.sample.payment.product.Dessert
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
internal abstract class DessertModule {
    @Binds
    @Named(DESSERT)
    internal abstract fun bindDessert(dessert: Dessert): BaseProduct
}