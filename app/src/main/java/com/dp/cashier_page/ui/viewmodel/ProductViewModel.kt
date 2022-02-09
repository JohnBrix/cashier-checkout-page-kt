package com.dp.cashier_page.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dp.cashier_page.domain.response.product.HttpProductList
import com.dp.cashier_page.domain.response.product.Item
import com.dp.cashier_page.repository.ProductRepository

class ProductViewModel: ViewModel() {
    var repository = ProductRepository()
    var itemsToCart = MutableLiveData<List<Item>>()

    fun getProduct(context:Context): LiveData<HttpProductList>{

        return repository.getProducts(context)
    }


    fun itemToCart(items: List<Item>) {
        itemsToCart.value = items
    }
}