package com.caper.pricechecker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.caper.pricechecker.R
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.modal.remote.RemoteShoppingItems
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel.shoppingItems.observe(this) { data ->
            Log.d("anton", "fucking yeah!!! ${data[0].id}")
        }

        mainViewModel.getShoppingItems()

    }
}