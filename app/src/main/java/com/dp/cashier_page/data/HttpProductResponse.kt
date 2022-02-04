package com.dp.cashier_page.data



data class HttpProductResponse(
    var itemList: List<HttpProductListItem>? = null,
    var resultMessage: String? = null
)