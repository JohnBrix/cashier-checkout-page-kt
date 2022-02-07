package com.dp.cashier_page.domain.request


import com.google.gson.annotations.SerializedName

data class HttpPosRequest(
    @SerializedName("cash")
    var cash: Double? = null,
    @SerializedName("change")
    var change: Double? = null,
    @SerializedName("dispenseBy")
    var dispenseBy: String? = null,
    @SerializedName("itemList")
    var itemList: List<HttpPosRequestListItem>? = null,
    @SerializedName("tax")
    var tax: Double? = null,
    @SerializedName("totalItems")
    var totalItems: Double? = null
)