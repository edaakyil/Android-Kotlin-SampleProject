package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment
import com.edaakyil.android.kotlin.app.sample.payment.constant.MENU_PAYMENT
import com.edaakyil.android.kotlin.app.sample.payment.product.Menu
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
abstract class MenuPaymentModule {
    @Binds
    @Named(MENU_PAYMENT)
    abstract fun bindMenu(menu: Menu): IProductPayment
}