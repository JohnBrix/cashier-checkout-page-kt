package com.dp.cashier_page.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dp.cashier_page.R
import com.dp.cashier_page.databinding.RecyclerProductBinding
import com.dp.cashier_page.domain.Item
import com.dp.cashier_page.ui.adapter.CheckoutAdapter
import com.dp.cashier_page.ui.adapter.InventoryAdapter
import com.dp.cashier_page.ui.viewmodel.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import de.starkling.shoppingcart.widget.CounterView

class ProductActivity : AppCompatActivity(), AddToCart {

    private lateinit var binding: RecyclerProductBinding
    private lateinit var vModel: ProductViewModel
    private var TAG = "ProductActivity: "
    private lateinit var inventoryAdapter: InventoryAdapter
    var itemToCart = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@ProductActivity, R.layout.recycler_product)
        vModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        binding.productViewModel = vModel
        binding.lifecycleOwner = this@ProductActivity


        binding.inventProgBar.visibility = View.VISIBLE
        vModel.getProduct(applicationContext).observe(this, Observer {
            Log.i(TAG, "${it}")


            if (it.resultMessage.equals("SUCCESS")) {

                binding.inventProgBar.visibility = View.GONE
                var dashboardRecycleView: RecyclerView? = null
                val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
                recyclerView.layoutManager =
                    GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
                dashboardRecycleView?.layoutManager = recyclerView.layoutManager

                recyclerView.adapter = it.itemList?.let { it1 ->
                    InventoryAdapter(it1, this)
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

    override fun onAddToCard(item: Item) {
        /*todo create validation here using hash dont create multiple add items with same id*/
        itemToCart.add(item)
        Toast.makeText(
            applicationContext,
            "Items: ${itemToCart.size}",
            Toast.LENGTH_SHORT
        ).show()
        println("Items: ${itemToCart.size}")
    }

    override fun openCheckout(callback: AddToCart) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.recycler_checkout, null, false)

        /*CREATE ADAPTER HERE*/

        var dialog: AlertDialog?
        val mBuilder = AlertDialog.Builder(
            view.context,
            android.R.style.Theme_Material_Light_NoActionBar_Fullscreen
        )
        /*   Theme_Material_Light_NoActionBar_Fullscreen*/
        mBuilder.setCancelable(false)
        mBuilder.setView(view)
        dialog = mBuilder.create()
        dialog.show()

        val window: Window? = dialog?.window
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        }
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        view.apply {
            var dashboardRecycleView: RecyclerView? = null
            val recyclerView = findViewById<RecyclerView>(R.id.checkoutRecycler)
            recyclerView.layoutManager = LinearLayoutManager(context)
            dashboardRecycleView?.layoutManager = recyclerView.layoutManager
            /*TODO CREATE COMPUTATION HERE TO CREATE POS*/
            recyclerView.adapter = CheckoutAdapter(itemToCart, callback, view)
        }
    }

    override fun checkout(
        view: View,
        count: Int,
        response: Item,
        counterValue: CounterView,
        subTotalSpecificItem: TextView
    ) {
        view.apply {

            computation(count, response, view, counterValue,subTotalSpecificItem)


        }

    }

}

private fun computation(
    count: Int,
    response: Item,
    view: View,
    counterView: CounterView,
    subTotalSpecificItem: TextView,

    ) {

    view.apply {
        var cash = findViewById(R.id.cash) as TextInputEditText
        var btn = findViewById(R.id.checkOut) as Button
        var productNameTextView = findViewById(R.id.productNameTextView) as TextView
        var priceTextView = findViewById(R.id.priceTextView) as TextView
        //TODO:COMPUTE THIS IN CHECKOUT ADAPTER
        // var totalSpecificItems = findViewById(R.id.subTotalSpecificItem) as TextView
        var totalItems = findViewById(R.id.totalItems) as TextView
        var tax = findViewById(R.id.tax) as TextView
        var grandTotal = findViewById(R.id.grandTotal) as TextView

        var price: Double = response.srpPrice!!
        var qtyToPriceTotal = count * price

        /*FOR EACH THE DATA*/


        if (count > response.quantity!!) {
            Toast.makeText(context, "${count}", Toast.LENGTH_SHORT).show()
            counterView.counterValue = response.quantity!! //Stoping the count

        } else {
            subTotalSpecificItem.text = qtyToPriceTotal.toString()
            priceTextView.setText(response.srpPrice.toString()) /*DISPLAY UI FROM RESPONSE*/

            var totalChange = findViewById(R.id.totalChange) as TextView
            btn.setOnClickListener {
                /*AUTO COMPUTE*/
                var pay: Double = cash.text.toString().toDouble()
                var computed: Double = pay - qtyToPriceTotal


                /*VAT COMPUTE*/
                var vat = qtyToPriceTotal * 0.12
                tax.setText(vat.toString())

                var subTotalItems = qtyToPriceTotal - vat

                totalChange.setText(computed.toString())
                totalItems.setText(subTotalItems.toString())
                grandTotal.setText(qtyToPriceTotal.toString())

                if (computed < -0.0) {
                    Toast.makeText(context, "Insufficient Cash!", Toast.LENGTH_SHORT)
                        .show()
                }

                cash.clearFocus();
            }
        }
    }
}


interface AddToCart {
    fun onAddToCard(item: Item)
    fun openCheckout(callback: AddToCart)
    fun checkout(
        view: View,
        count: Int,
        response: Item,
        counterValue: CounterView,
        subTotalSpecificItem: TextView
    )
}
