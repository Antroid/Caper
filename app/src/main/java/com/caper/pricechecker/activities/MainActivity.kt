package com.caper.pricechecker.activities

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.caper.pricechecker.R
import com.caper.pricechecker.fragments.base.BaseFragment
import com.caper.pricechecker.fragments.cart.CartFragment
import com.caper.pricechecker.fragments.shopping.ShoppingFragment
import com.caper.pricechecker.modal.local.ShoppingItems
import com.caper.pricechecker.viewmodels.CartViewModel
import com.caper.pricechecker.viewmodels.MainViewModel
import com.caper.pricechecker.viewmodels.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @set:Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var cartViewModel: CartViewModel

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        cartViewModel = ViewModelProviders.of(this, viewModelFactory).get(CartViewModel::class.java)

        if(mainViewModel.fragmentToOpen == ShoppingFragment.TAG) {
            showFragment(ShoppingFragment.newInstance())
        }else if(mainViewModel.fragmentToOpen == CartFragment.TAG){
            showFragment(CartFragment.newInstance(null))
        }
    }

    fun showFragment(fragment: BaseFragment){

        with(supportFragmentManager){
            if(fragments.isNotEmpty()){
                val currFrag = fragments[fragments.size-1] as BaseFragment
                if(currFrag.getFragTag() == fragment.getFragTag())
                    return
            }
        }

        mainViewModel.fragmentToOpen = fragment.getFragTag()

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack(fragment.getFragTag())
        ft.commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        this.menu = menu
        changeOptionMenuByCurrentFragment()

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

    fun changeOptionMenuByCurrentFragment() {
        val menuIsVisible = mainViewModel.fragmentToOpen == ShoppingFragment.TAG
        menu?.let {
            it.findItem(R.id.cart).isVisible = menuIsVisible
            it.findItem(R.id.search).isVisible = menuIsVisible
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){
            R.id.cart ->{
                showFragment(CartFragment.newInstance(null))
            }
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
        if(supportFragmentManager.backStackEntryCount > 1){
           supportFragmentManager.popBackStackImmediate()
            mainViewModel.fragmentToOpen = (supportFragmentManager.fragments[supportFragmentManager.fragments.size-1] as BaseFragment).getFragTag()
            changeOptionMenuByCurrentFragment()
        }else{
           finish()
        }
    }

}