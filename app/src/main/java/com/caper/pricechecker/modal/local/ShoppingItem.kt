package com.caper.pricechecker.modal.local

data class ShoppingItem(
    val id: String,
    val name: String,
    val price: String,
    val qrUrl: String,
    val thumbnail: String
){
    var isSelected: Boolean = false
}