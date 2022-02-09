package com.dp.cashier_page.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dp.cashier_page.domain.response.product.HttpProductList
import com.google.gson.Gson
import com.zaiko.data.constant.PomsV1
import com.zaiko.data.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository: Product {
    private var retrofit = RetrofitClient()
    private val TAG = "ProductRepository: "

    fun getProducts(context: Context)
            : LiveData<HttpProductList> {
        var liveData = MutableLiveData<HttpProductList>()

        val userService: PomsV1 = retrofit.getProduct(context)
            .create(PomsV1::class.java)
        var call: Call<HttpProductList> =
            userService.getProduct()

        call.enqueue(object : Callback<HttpProductList?> {
            override fun onResponse(
                call: Call<HttpProductList?>,
                response: Response<HttpProductList?>
            ) {

                if (response.code() == 200) {
                    Log.i(TAG, "${response.body()}")
                    liveData.value = response.body()
                } else {
                    var product = HttpProductList()
                    var gson = Gson();
                    var adapter = gson.getAdapter(HttpProductList::class.java)

                    if (response.errorBody() != null)
                        product = adapter.fromJson(response.errorBody()?.string())

                    Log.i(TAG, "${product}")
                    liveData.value = product
                }

            }

            override fun onFailure(call: Call<HttpProductList?>, t: Throwable) {
                Log.e("OnError: ", "${call} & ${t.message}")
                var prod = HttpProductList()
                var httpProd = HttpProductList()
                httpProd.resultMessage = "NO_CONTENT"
                Log.i(TAG, "${prod}")
                liveData.value = prod
            }

        })
        Log.i(TAG, "${liveData.value}")
        return liveData
    }
}