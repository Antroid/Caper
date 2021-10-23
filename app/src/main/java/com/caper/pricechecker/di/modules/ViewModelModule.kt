package com.caper.pricechecker.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caper.pricechecker.di.scopes.ViewModelKey
import com.caper.pricechecker.viewmodels.CartViewModel
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun bindMCartViewModel(cartViewModel: CartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}