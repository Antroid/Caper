package com.caper.pricechecker.activities.cart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.caper.pricechecker.Consts
import com.caper.pricechecker.R
import com.caper.pricechecker.interfaces.CartItemClick
import com.caper.pricechecker.modal.local.ShoppingItem
import com.caper.pricechecker.viewmodels.CartViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartActivity: DaggerAppCompatActivity(), CartItemClick {

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter: CartItemsAdapter


    lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartViewModel = ViewModelProviders.of(this, viewModelFactory).get(CartViewModel::class.java)



        adapter = CartItemsAdapter(this)
        val shoppingLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cartRecycleViewItems.layoutManager = shoppingLayoutManager
        cartRecycleViewItems.adapter = adapter

        cartViewModel.total.observe(this) { totalPrice ->
            total.text = getString(R.string.total_price, totalPrice)
        }

        if(intent.hasExtra(Consts.ITEM_INDEX_SELECTED_TO_CARD)){
            val selectedShoppingItem = intent.getParcelableExtra<ShoppingItem>(Consts.ITEM_INDEX_SELECTED_TO_CARD)
            selectedShoppingItem?.let{
                cartViewModel.updateItem(it)
                val shoppingItems = cartViewModel.getShoppingCart()
                adapter.setData(shoppingItems)
            }
            changeComponentsVisibility()
        }
    }

    override fun onAdd(position: Int) {
        adapter.getItem(position).quantity++
        cartViewModel.updateItem(adapter.getItem(position))
        adapter.notifyItemChanged(position)
    }

    override fun onSub(position: Int) {
        adapter.getItem(position).quantity--
        if(adapter.getItem(position).quantity < 0) {
            adapter.getItem(position).quantity = 0
        }
        adapter.notifyItemChanged(position)

        cartViewModel.updateItem(adapter.getItem(position))

        Handler(Looper.getMainLooper()).postDelayed({
            if(cartViewModel.isCartEmpty()){
               changeComponentsVisibility()
            }else{
                if(adapter.getItem(position).quantity == 0){
                    adapter.notifyItemRemoved(position)
                }
            }
        },5000)


    }

    private fun changeComponentsVisibility() {
        cartRecycleViewItems.isVisible = !cartViewModel.isCartEmpty()
        no_items_in_cart.isVisible = cartViewModel.isCartEmpty()
        total.isVisible = !cartViewModel.isCartEmpty()
    }

}