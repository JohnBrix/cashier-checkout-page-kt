package com.dp.cashier_page.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dp.cashier_page.domain.HttpProductList
import com.dp.cashier_page.repository.ProductRepository

class ProductViewModel: ViewModel() {
    var repository = ProductRepository()

    fun getProduct(context:Context): LiveData<HttpProductList>{

        return repository.getProducts(context)
    }
}