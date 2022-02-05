package com.dp.cashier_page.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dp.cashier_page.R
import com.dp.cashier_page.databinding.RecyclerProductBinding
import com.dp.cashier_page.ui.adapter.InventoryAdapter
import com.dp.cashier_page.ui.viewmodel.ProductViewModel

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: RecyclerProductBinding
    private lateinit var vModel: ProductViewModel
    private var TAG = "ProductActivity: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@ProductActivity, R.layout.recycler_product)
        vModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        binding.productViewModel = vModel
        binding.lifecycleOwner = this@ProductActivity


        binding.inventProgBar.visibility = View.VISIBLE
        vModel.getProduct(applicationContext).observe(this, Observer {
            it
            Log.i(TAG, "${it}")


            if (it.resultMessage.equals("SUCCESS")) {

                binding.inventProgBar.visibility = View.GONE
                var dashboardRecycleView: RecyclerView? = null
                val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
                recyclerView.layoutManager =
                    GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
                dashboardRecycleView?.layoutManager = recyclerView.layoutManager

                recyclerView.adapter = it.itemList?.let { it1 ->
                    InventoryAdapter(it1)

                }

            } else if (it.resultMessage.equals("NO_CONTENT")) {
                binding.inventProgBar.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    "NO_CONTENT",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.inventProgBar.visibility = View.GONE
                Toast.makeText(
                    applicationContext,
                    "Please Check your internet and try again later!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


    }

}