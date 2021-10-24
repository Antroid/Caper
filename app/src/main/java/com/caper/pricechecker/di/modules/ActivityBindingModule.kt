package com.caper.pricechecker.di.modules

import com.caper.pricechecker.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [(MainFragmentBindingModule::class)])
    internal abstract fun bindMainActivity(): MainActivity
}