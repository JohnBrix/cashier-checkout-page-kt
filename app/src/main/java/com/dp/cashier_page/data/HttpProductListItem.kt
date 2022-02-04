package com.dp.cashier_page.data


data class HttpProductListItem(
    var brandModel: String? = null,

    var category: String? = null,

    var description: String? = null,

    var itemName: String? = null,

    var itemPicture: String? = null,

    var productId: Int? = null,

    var quantity: Int? = null,

    var srpPrice: Double? = null,

    var stockIn: Any? = null
)