package com.caper.pricechecker.di.modules

import com.caper.pricechecker.fragments.cart.CartFragment
import com.caper.pricechecker.fragments.shopping.ShoppingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun provideShoppingFragment(): ShoppingFragment

    @ContributesAndroidInjector
    internal abstract fun provideCartFragment(): CartFragment

}