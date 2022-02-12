package com.dp.final_receipt_zm.ui

enum class RTransactionType { plus, minus }

class RTransaction {

    var itemName: String = ""
    var custName: String = ""
    var transType: RTransactionType = RTransactionType.plus
    var pricePerUnit: Double = 0.0
    var quantity: Int = 0
    var totalPrice: Double = 0.0
    var transactionDateStr: String = ""

    constructor() {
    }
}
