package com.dp.cashier_page.domain.response.pos


import com.google.gson.annotations.SerializedName

data class HttpPos(
    @SerializedName("response")
    var response: HttpPosResponse? = null
)