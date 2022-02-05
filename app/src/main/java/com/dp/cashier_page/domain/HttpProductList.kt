package com.dp.cashier_page.domain


import com.google.gson.annotations.SerializedName

data class HttpProductList(
    @SerializedName("itemList")
    var itemList: List<Item>? = null,
    @SerializedName("resultMessage")
    var resultMessage: String? = null
)