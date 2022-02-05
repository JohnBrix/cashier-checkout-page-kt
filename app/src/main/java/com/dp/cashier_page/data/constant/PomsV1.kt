package com.zaiko.data.constant


import com.dp.cashier_page.domain.HttpProductList
import retrofit2.Call
import retrofit2.http.GET

interface PomsV1 {

    @GET("/product")
    fun getProduct(): Call<HttpProductList>
}