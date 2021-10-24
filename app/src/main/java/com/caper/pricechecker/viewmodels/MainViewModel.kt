package com.caper.pricechecker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.repositories.ShoppingRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class MainViewModel @Inject
constructor() : ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    var filteredItems = MutableLiveData<ShoppingItems>()
    val shoppingItems = MutableLiveData<ShoppingItems>()
    private val disposable: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var shoppingRepo: ShoppingRepo

    fun getShoppingItems(){
        disposable.add(shoppingRepo.getShoppingItems().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<ShoppingItems>(){
                override fun onNext(t: ShoppingItems) {
                    shoppingItems.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }
            }))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}