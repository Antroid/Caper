package com.caper.pricechecker.modal.local


import com.google.gson.annotations.SerializedName

data class ShoppingItem(
    val id: String,
    val name: String,
    val price: String,
    val qrUrl: String,
    val thumbnail: String
)