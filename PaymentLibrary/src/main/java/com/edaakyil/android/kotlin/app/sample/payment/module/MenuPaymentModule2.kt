package com.edaakyil.android.kotlin.app.sample.payment.module

import com.edaakyil.android.kotlin.app.sample.payment.IProductPayment
import com.edaakyil.android.kotlin.app.sample.payment.constant.MENU_PAYMENT2
import com.edaakyil.android.kotlin.app.sample.payment.product.Menu2
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
abstract class MenuPaymentModule2 {
    @Binds
    @Named(MENU_PAYMENT2)
    abstract fun bindMenu(menu: Menu2): IProductPayment
}