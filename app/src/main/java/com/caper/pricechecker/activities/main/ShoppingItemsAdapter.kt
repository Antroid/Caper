package com.caper.pricechecker.activities.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caper.pricechecker.R
import com.caper.pricechecker.interfaces.ShoppingItemClick
import com.caper.pricechecker.modal.local.ShoppingItem
import com.caper.pricechecker.modal.local.ShoppingItems

class ShoppingItemsAdapter(private val listener: ShoppingItemClick): RecyclerView.Adapter<ShoppingItemsAdapter.ShoppingHolder>() {

    private var data = ShoppingItems()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingHolder =
        ShoppingHolder(LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false))

    override fun onBindViewHolder(holder: ShoppingHolder, position: Int) {
        holder.bind(position, data[position])
    }

    fun getItem(position: Int): ShoppingItem = data[position]

    override fun getItemCount(): Int = data.size

    inner class ShoppingHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.item_name)
        var itemPoster: ImageView = itemView.findViewById(R.id.item_poster)
        var itemPrice: TextView = itemView.findViewById(R.id.item_price)
        fun bind(position: Int, shoppingItem: ShoppingItem) {
            itemName.text = shoppingItem.name
            Glide.with(itemPoster.context).load(shoppingItem.thumbnail).into(itemPoster)
            itemPrice.text = itemPrice.context.getString(R.string.price_holder,shoppingItem.price)
            itemView.isSelected = shoppingItem.isSelected
            itemView.setOnClickListener {
                listener.onItemClick(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ShoppingItems){
        this.data = data
        notifyDataSetChanged()
    }

    fun changeSelectedState(selectedItemIndex: Int, selectedState: Boolean) {
        data[selectedItemIndex].isSelected = selectedState
        notifyItemChanged(selectedItemIndex)
    }

}