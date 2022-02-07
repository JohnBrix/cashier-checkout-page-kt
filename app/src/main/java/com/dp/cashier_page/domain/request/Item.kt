package com.dp.cashier_page.domain.request


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("itemName")
    var itemName: String? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("srpPrice")
    var srpPrice: Double? = null
)