package com.caper.pricechecker.di.components

import android.app.Application
import com.caper.pricechecker.di.modules.ActivityBindingModule
import com.caper.pricechecker.di.modules.ApplicationModule
import com.caper.pricechecker.di.modules.ContextModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, ApplicationModule::class,
    AndroidSupportInjectionModule::class, ActivityBindingModule::class])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(application: Application)

}