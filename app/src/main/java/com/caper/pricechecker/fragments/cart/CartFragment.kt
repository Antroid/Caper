package com.caper.pricechecker.fragments.cart

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.caper.pricechecker.Consts
import com.caper.pricechecker.R
import com.caper.pricechecker.fragments.base.BaseFragment
import com.caper.pricechecker.interfaces.CartItemClick
import com.caper.pricechecker.modal.local.ShoppingItem
import com.caper.pricechecker.viewmodels.CartViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartFragment: BaseFragment(), CartItemClick {

    companion object{
        const val TAG = "CartFragment"
        fun newInstance(shoppingItem: ShoppingItem): CartFragment{
            val bundle = Bundle()
            bundle.putParcelable(Consts.ITEM_SELECTED_TO_CARD_KEY, shoppingItem)
            val cartFragment = CartFragment()
            cartFragment.arguments = bundle
            return cartFragment
        }
    }

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter: CartItemsAdapter


    lateinit var cartViewModel: CartViewModel


    override fun initObservables() {
        cartViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(CartViewModel::class.java)
        cartViewModel.total.observe(this) { totalPrice ->
            total.text = getString(R.string.total_price, totalPrice)
        }
    }

    override fun layoutRes(): Int {
        return R.layout.cart_fragment
    }

    override fun initUI() {
        adapter = CartItemsAdapter(this)
        val shoppingLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartRecycleViewItems.layoutManager = shoppingLayoutManager
        cartRecycleViewItems.adapter = adapter
    }

    override fun initLogic() {
        arguments?.let {
            if(it.containsKey(Consts.ITEM_SELECTED_TO_CARD_KEY)){
                val selectedShoppingItem = it.getParcelable<ShoppingItem>(Consts.ITEM_SELECTED_TO_CARD_KEY)
                selectedShoppingItem?.let{ shoppingItem ->
                    cartViewModel.addCartItem(shoppingItem)
                    val shoppingItems = cartViewModel.getShoppingCart()
                    adapter.setData(shoppingItems)
                }
                changeComponentsVisibility()
            }
        }

    }

    override fun onAdd(position: Int) {
        adapter.getItem(position).quantity++
        cartViewModel.updateItem(adapter.getItem(position))
        adapter.notifyItemChanged(position)
    }

    override fun onSub(position: Int) {
        adapter.getItem(position).quantity--
        cartViewModel.updateItem(adapter.getItem(position))

        if(adapter.getItem(position).quantity <= 0) {
            adapter.removeItem(position)
        }else{
            adapter.notifyItemChanged(position)
        }

        changeComponentsVisibility()

    }

    private fun changeComponentsVisibility() {
        cartRecycleViewItems.isVisible = !cartViewModel.isCartEmpty()
        no_items_in_cart.isVisible = cartViewModel.isCartEmpty()
        total.isVisible = !cartViewModel.isCartEmpty()
    }

}



