package com.dp.cashier_page.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dp.cashier_page.domain.request.HttpPosRequest
import com.dp.cashier_page.domain.response.pos.HttpPos
import com.dp.cashier_page.domain.response.product.HttpProductList
import com.dp.cashier_page.domain.response.product.Item
import com.dp.cashier_page.repository.impl.PosRepository
import com.dp.cashier_page.repository.impl.ProductRepository

class ProductViewModel: ViewModel() {
    var productRepository = ProductRepository()
    var posRepository = PosRepository()
    var itemsToCart = MutableLiveData<List<Item>>()

    fun getProduct(context:Context): LiveData<HttpProductList>{

        return productRepository.getProducts(context)
    }
    fun createPos(context:Context,request: HttpPosRequest): LiveData<HttpPos>{

        return posRepository.createPos(context,request)
    }


    fun itemToCart(items: List<Item>) {
        itemsToCart.value = items
    }
}