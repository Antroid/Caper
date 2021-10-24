package com.caper.pricechecker.fragments.shopping

import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.caper.pricechecker.R
import com.caper.pricechecker.activities.MainActivity
import com.caper.pricechecker.fragments.base.BaseFragment
import com.caper.pricechecker.fragments.cart.CartFragment
import com.caper.pricechecker.interfaces.ShoppingItemClick
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.shopping_fragment.*
import javax.inject.Inject

class ShoppingFragment: BaseFragment(), ShoppingItemClick {

    companion object{
        private const val NO_ITEM_INDEX_SELECTED = -1
        const val TAG = "ShoppingFragment"
        fun newInstance(): ShoppingFragment{
            return ShoppingFragment()
        }

    }

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter: ShoppingItemsAdapter

    private var selectedItemIndex = -1

    private lateinit var mainViewModel: MainViewModel

    override fun layoutRes(): Int {
        return R.layout.shopping_fragment
    }

    override fun initLogic() {
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
                val selectedItem = adapter.getItem(selectedItemIndex)
                selectedItem.isSelected = false
                selectedItemIndex = NO_ITEM_INDEX_SELECTED
                val cartFragment = CartFragment.newInstance(selectedItem)
                activity?.let {
                    (it as MainActivity).showFragment(cartFragment, TAG)
                }
            }
        }

    }

    override fun initObservables() {
        mainViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        mainViewModel.shoppingItems.observe(this) { data ->
            adapter.setData(data)
        }

        mainViewModel.filteredItems.observe(this){ filteredShoppingItems->
            adapter.setData(filteredShoppingItems)
        }

    }

    override fun onItemClick(index: Int) {
        if (selectedItemIndex != NO_ITEM_INDEX_SELECTED) {
            mainViewModel.shoppingItems.value?.get(selectedItemIndex)?.isSelected = false
            if (selectedItemIndex < adapter.itemCount) {
                adapter.changeSelectedState(selectedItemIndex, false)
            }
        }
        if (selectedItemIndex != index) {
            adapter.changeSelectedState(index, true)
            selectedItemIndex = index
        }else{
            selectedItemIndex = NO_ITEM_INDEX_SELECTED
        }
    }

}



