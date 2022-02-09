package com.zaiko.data.constant


import com.dp.cashier_page.domain.request.HttpPosRequest
import com.dp.cashier_page.domain.response.pos.HttpPos
import com.dp.cashier_page.domain.response.product.HttpProductList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface PomsV1 {

    @GET("/product")
    fun getProduct(): Call<HttpProductList>

    @POST("/product/pos")
    fun createPos(request: HttpPosRequest): Call<HttpPos>
}