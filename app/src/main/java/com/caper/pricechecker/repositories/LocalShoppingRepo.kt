package com.caper.pricechecker.repositories

import android.content.Context
import com.caper.pricechecker.Consts
import com.caper.pricechecker.modal.remote.RemoteShoppingItems
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

class LocalShoppingRepo@Inject constructor(private val context: Context)
{
    fun getShoppingItems(): Single<RemoteShoppingItems> {
        val fileInString: String = context.assets.open(Consts.JSON_FILE).bufferedReader().use { it.readText() }
        val shoppingCart = Gson().fromJson(fileInString, RemoteShoppingItems::class.java)
        return Single.just(shoppingCart)
    }
}