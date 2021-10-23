package com.caper.pricechecker.repositories

import com.caper.pricechecker.modal.local.ShoppingItem
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.modal.remote.RemoteShoppingItems
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingRepo @Inject constructor(
    private var rShoppingRepo: LocalShoppingRepo
){
    fun getShoppingItems(): Observable<ShoppingItems> {
        val singleRankingMovies = rShoppingRepo.getShoppingItems().flatMapMaybe { res ->
            Maybe.just(buildLocalShoppingItems(res))
        }
        return singleRankingMovies.toObservable()
    }

    private fun buildLocalShoppingItems(res: RemoteShoppingItems): ShoppingItems {
        val lList = ShoppingItems()

        for(item in res){
            lList.add(ShoppingItem(item.id, item.name, item.price, item.qrUrl, item.thumbnail))
        }
        return lList
    }
}