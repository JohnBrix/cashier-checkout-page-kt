package com.dp.cashier_page.domain.response.pos


import com.google.gson.annotations.SerializedName

data class HttpPosResponse(
    @SerializedName("cash")
    var cash: Double? = null,
    @SerializedName("change")
    var change: Double? = null,
    @SerializedName("dateOfTrans")
    var dateOfTrans: String? = null,
    @SerializedName("itemList")
    var itemList: List<HttpPosResponseListItem>? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("resultMessage")
    var resultMessage: String? = null,
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("tax")
    var tax: Double? = null,
    @SerializedName("total")
    var total: Double? = null,
    @SerializedName("transactionNumber")
    var transactionNumber: String? = null
)