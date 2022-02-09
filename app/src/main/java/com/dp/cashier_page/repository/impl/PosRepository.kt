package com.dp.cashier_page.repository.impl

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dp.cashier_page.domain.request.HttpPosRequest
import com.dp.cashier_page.domain.response.pos.HttpPos
import com.dp.cashier_page.domain.response.pos.HttpPosResponse
import com.dp.cashier_page.domain.response.product.HttpProductList
import com.dp.cashier_page.repository.Pos
import com.google.gson.Gson
import com.zaiko.data.constant.PomsV1
import com.zaiko.data.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PosRepository: Pos {
    private var retrofit = RetrofitClient()
    private val TAG = "PosRepository: "

    fun createPos(context: Context,request: HttpPosRequest)
            : LiveData<HttpPos> {
        var liveData = MutableLiveData<HttpPos>()

        val userService: PomsV1 = retrofit.createPos(context)
            .create(PomsV1::class.java)
        var call: Call<HttpPos> =
            userService.createPos(request)

        call.enqueue(object : Callback<HttpPos?> {
            override fun onResponse(
                call: Call<HttpPos?>,
                response: Response<HttpPos?>
            ) {

                if (response.code() == 200) {
                    Log.i(TAG, "${response.body()}")
                    liveData.value = response.body()
                } else {
                    var product = HttpPos()
                    var gson = Gson();
                    var adapter = gson.getAdapter(HttpPos::class.java)

                    if (response.errorBody() != null)
                        product = adapter.fromJson(response.errorBody()?.string())

                    Log.i(TAG, "${product}")
                    liveData.value = product
                }

            }

            override fun onFailure(call: Call<HttpPos?>, t: Throwable) {
                Log.e("OnError: ", "${call} & ${t.message}")
                var prod = HttpPos()
                var httpProd = HttpPosResponse()
                httpProd.resultMessage = "NO_CONTENT"
                prod.response = httpProd
                Log.i(TAG, "${prod}")
                liveData.value = prod
            }

        })
        Log.i(TAG, "${liveData.value}")
        return liveData
    }
}