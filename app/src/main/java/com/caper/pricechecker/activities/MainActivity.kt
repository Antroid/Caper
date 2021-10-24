package com.caper.pricechecker.activities

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.caper.pricechecker.R
import com.caper.pricechecker.fragments.base.BaseFragment
import com.caper.pricechecker.fragments.shopping.ShoppingFragment
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        showFragment(ShoppingFragment.newInstance(), ShoppingFragment.TAG)

    }

    fun showFragment(fragment: BaseFragment, tag: String){
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack(tag)
        ft.commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

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
            mainViewModel.filteredItems.value = filteredShoppingItems
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.isNotEmpty()){
            supportFragmentManager.popBackStackImmediate()
        }else{
            super.onBackPressed()
        }
    }

}