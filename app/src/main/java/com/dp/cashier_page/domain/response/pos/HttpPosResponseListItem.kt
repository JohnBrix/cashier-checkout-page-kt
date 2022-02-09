package com.dp.cashier_page.domain.response.pos


import com.google.gson.annotations.SerializedName

data class HttpPosResponseListItem(
    @SerializedName("brandModel")
    var brandModel: String? = null,
    @SerializedName("itemName")
    var itemName: String? = null,
    @SerializedName("srpPrice")
    var srpPrice: Double? = null
)