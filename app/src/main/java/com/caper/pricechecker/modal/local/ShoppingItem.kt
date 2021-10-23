package com.caper.pricechecker.modal.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShoppingItem(
    val id: String,
    val name: String,
    val price: Float,
    val qrUrl: String,
    val thumbnail: String,
    var quantity: Int
): Parcelable {
    var isSelected: Boolean = false
}