package com.caper.pricechecker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caper.pricechecker.modal.local.ShoppingItem
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.repositories.ShoppingRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CartViewModel @Inject
constructor() : ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    val total = MutableLiveData<Float>()
    private val cartItems = HashMap<String, ShoppingItem>()

    fun updateItem(item: ShoppingItem){
        cartItems[item.id] = item
        calculateTotal()
    }

    fun getShoppingCart(): ShoppingItems{
        val list = ShoppingItems()
        for(key in cartItems.keys){
            cartItems[key]?.let {
                list.add(it)
            }
        }
        return list
    }

    fun isCartEmpty() = cartItems.isEmpty()

    private fun calculateTotal(){
        var total = 0f
        for(key in cartItems.keys){
            val item = cartItems[key]
            item?.let {
                total += it.price * it.quantity
            }
        }
        this.total.value = total
    }


}