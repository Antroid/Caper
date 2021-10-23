package com.caper.pricechecker.fragments.shopping

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.caper.pricechecker.Consts
import com.caper.pricechecker.R
import com.caper.pricechecker.activities.cart.CartActivity
import com.caper.pricechecker.activities.main.ShoppingItemsAdapter
import com.caper.pricechecker.fragments.base.BaseFragment
import com.caper.pricechecker.interfaces.ShoppingItemClick
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ShoppingFragment: BaseFragment(), ShoppingItemClick {

    companion object{
        private const val NO_ITEM_INDEX_SELECTED = -1
    }

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter: ShoppingItemsAdapter

    private var selectedItemIndex = -1

    lateinit var mainViewModel: MainViewModel

    override fun layoutRes(): Int {
        return R.layout.shopping_fragment
    }

    override fun initLogic() {
        mainViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        mainViewModel.getShoppingItems()
    }

    override fun initUI() {
        adapter = ShoppingItemsAdapter(this)
        val shoppingLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        shoppingRecycleViewItems.layoutManager = shoppingLayoutManager
        shoppingRecycleViewItems.adapter = adapter

        addToCartBtn.setOnClickListener{
            if(selectedItemIndex == NO_ITEM_INDEX_SELECTED){
                Toast.makeText(requireContext(), getString(R.string.no_item_selected), Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(requireActivity(), CartActivity::class.java)
                val selectedItem = adapter.getItem(selectedItemIndex)
                selectedItem.quantity++
                intent.putExtra(Consts.ITEM_INDEX_SELECTED_TO_CARD, selectedItem)
                startActivity(intent)
            }
        }

    }

    override fun initObservables() {
        mainViewModel.shoppingItems.observe(this) { data ->
            adapter.setData(data)
        }
    }

    override fun onItemClick(index: Int) {
        if (selectedItemIndex != NO_ITEM_INDEX_SELECTED) {
            adapter.changeSelectedState(selectedItemIndex, false)
        }
        if (selectedItemIndex != index) {
            adapter.changeSelectedState(index, true)
            selectedItemIndex = index
        }else{
            selectedItemIndex = NO_ITEM_INDEX_SELECTED
        }
    }

}



