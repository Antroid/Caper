package com.caper.pricechecker.fragments.cart


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caper.pricechecker.R
import com.caper.pricechecker.interfaces.CartItemClick
import com.caper.pricechecker.modal.local.ShoppingItem
import com.caper.pricechecker.modal.local.ShoppingItems

class CartItemsAdapter(val listener: CartItemClick): RecyclerView.Adapter<CartItemsAdapter.CartHolder>() {

    private var data = ShoppingItems()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder =
        CartHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        holder.bind(position, data[position])
    }

    fun getItem(position: Int): ShoppingItem = data[position]

    override fun getItemCount(): Int = data.size

    inner class CartHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.item_name)
        var itemPoster: ImageView = itemView.findViewById(R.id.item_poster)
        var itemPrice: TextView = itemView.findViewById(R.id.item_price)

        var itemQuality: TextView = itemView.findViewById(R.id.item_quantity)
        var add: Button = itemView.findViewById(R.id.add)
        var sub: Button = itemView.findViewById(R.id.sub)

        fun bind(position: Int, cartItem: ShoppingItem) {
            itemName.text = cartItem.name
            Glide.with(itemPoster.context).load(cartItem.thumbnail).into(itemPoster)
            itemPrice.text = itemPrice.context.getString(R.string.price_holder, cartItem.price)
            itemQuality.text = cartItem.quantity.toString()
            add.setOnClickListener {
                listener.onAdd(position)
            }
            sub.setOnClickListener {
                listener.onSub(position)
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

    fun removeItem(position: Int){
        data.removeAt(position)
        notifyItemRemoved(position)
    }

}