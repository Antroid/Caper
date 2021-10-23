package com.caper.pricechecker.activities.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.caper.pricechecker.R
import com.caper.pricechecker.interfaces.ShoppingItemClick
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), ShoppingItemClick {

    companion object{
        private val NO_ITEM_INDEX_SELECTED = -1
    }

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter: ShoppingItemsAdapter

    private var selectedItemIndex = -1

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        adapter = ShoppingItemsAdapter(this)
        val movieLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        shoppingRecycleViewItems.layoutManager = movieLayoutManager
        shoppingRecycleViewItems.adapter = adapter

        mainViewModel.shoppingItems.observe(this) { data ->
            adapter.setData(data)
        }

        mainViewModel.getShoppingItems()

        addToCartBtn.setOnClickListener{
            if(selectedItemIndex == NO_ITEM_INDEX_SELECTED){
                Toast.makeText(this, getString(R.string.no_item_selected),Toast.LENGTH_LONG).show()
            }else{
                //TODO
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    updateSearch(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    updateSearch(newText)
                    return false
                }
            })
        }

        return true
    }

    private fun updateSearch(query: String) {
        val rawData = mainViewModel.shoppingItems.value?.filter { it.id.contains(query) }?.toMutableList()
        rawData?.let{
            val filteredShoppingItems = ShoppingItems()
            for(item in rawData){
                filteredShoppingItems += item
            }
            adapter.setData(filteredShoppingItems)
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