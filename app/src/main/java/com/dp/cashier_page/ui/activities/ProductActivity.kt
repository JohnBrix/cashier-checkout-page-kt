package com.dp.cashier_page.ui.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dp.cashier_page.R
import com.dp.cashier_page.databinding.RecyclerProductBinding
import com.dp.cashier_page.domain.request.HttpPosRequest
import com.dp.cashier_page.domain.request.HttpPosRequestListItem
import com.dp.cashier_page.domain.response.product.Item
import com.dp.cashier_page.ui.adapter.CheckoutAdapter
import com.dp.cashier_page.ui.adapter.InventoryAdapter
import com.dp.cashier_page.ui.viewmodel.ProductViewModel
import com.google.android.material.textfield.TextInputEditText
import de.starkling.shoppingcart.widget.CounterView
import java.util.stream.Collectors


class ProductActivity : AppCompatActivity(), AddToCart {

    private lateinit var binding: RecyclerProductBinding
    private lateinit var vModel: ProductViewModel
    private var TAG = "ProductActivity: "
    private lateinit var inventoryAdapter: InventoryAdapter
    var itemToCart = mutableListOf<Item>()
    lateinit var checkoutAdapter: CheckoutAdapter
    var totalAmount :Double = 0.0

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
                    InventoryAdapter(it1, this,this,vModel)
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

    fun productDisplay(){
        binding.inventProgBar.visibility = View.VISIBLE
        binding.inventProgBar.requestFocus()
        vModel.getProduct(applicationContext).observe(this, Observer {
            Log.i(TAG, "${it}")


            if (it.resultMessage.equals("SUCCESS")) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                binding.inventProgBar.visibility = View.GONE
                var dashboardRecycleView: RecyclerView? = null
                val recyclerView = findViewById<RecyclerView>(R.id.productRecyclerView)
                recyclerView.layoutManager =
                    GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
                dashboardRecycleView?.layoutManager = recyclerView.layoutManager

                recyclerView.adapter = it.itemList?.let { it1 ->
                    InventoryAdapter(it1, this,this,vModel)
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

        itemToCart.add(item)

        Toast.makeText(
            applicationContext,
            "Items: ${itemToCart.size}",
            Toast.LENGTH_SHORT
        ).show()
        println("Items: ${itemToCart.size}")
    }



    override fun openCheckout(
        callback: AddToCart,
        lifecycleOwner: LifecycleOwner,
        vModel: ProductViewModel
    ) {
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

            checkoutAdapter = CheckoutAdapter(itemToCart, callback, view,lifecycleOwner,vModel,dialog)
            recyclerView.adapter = checkoutAdapter
        }
    }
    var pos:Int = 0
    override fun checkout(
        view: View,
        count: Int,
        response: Item,
        counterValue: CounterView,
        subTotalSpecificItem: TextView,
        priceTextView: TextView,
        exit: ImageView,
        isAdded: Boolean,
        lifecycleOwner: LifecycleOwner,
        vModel: ProductViewModel,
        checkOutDialog: AlertDialog,
        position: Int
    ) {
        pos = position

        view.apply {
            computation(count, response, view, counterValue, subTotalSpecificItem, priceTextView, isAdded,lifecycleOwner,vModel,checkOutDialog)
        }
    }
    var current = mutableListOf<Item>()

    fun computation(
        count: Int,
        response: Item,
        view: View,
        counterView: CounterView,
        subTotalSpecificItem: TextView,
        priceTextView: TextView,
        isAdded: Boolean,
        lifecycleOwner: LifecycleOwner,
        vModel: ProductViewModel,
        checkOutDialog: AlertDialog,
    ) {
        println("IsAdded: $isAdded")
        view.apply {
            var cash = findViewById(R.id.cash) as TextInputEditText
            var btn = findViewById(R.id.checkOut) as Button
            var totalItems = findViewById(R.id.totalItems) as TextView
            var tax = findViewById(R.id.tax) as TextView
            var grandTotal = findViewById(R.id.grandTotal) as TextView
            var totalChange = findViewById(R.id.totalChange) as TextView







            var qtyToPriceTotal = count * response.srpPrice!!

            /* += pinaplus nya yung srpPrice mo */
            if(!isAdded) totalAmount -= response.srpPrice!!
            else totalAmount += response.srpPrice!!

            priceTextView.text = response.srpPrice.toString() /*original price item*/
            println("specificItem: " + totalAmount)

            checkoutAdapter.apply{
                vh.exit.setOnClickListener {
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, getItemCount() - pos)

                    deleteItem(pos)
                }
            }

            if (count > response.quantity!!) {
                Toast.makeText(context, "Cannot increment due item insufficient: ${count}", Toast.LENGTH_SHORT).show()
                counterView.counterValue = response.quantity!! //Stoping the count
                totalAmount -= response.srpPrice!!
            }

            else {
                subTotalSpecificItem.text = qtyToPriceTotal.toString() /*Subtotal specific item*/

                var vat = totalAmount * 0.12
                var extractVat = "%.2f".format(vat)

                totalItems.setText(totalAmount.toString())
                tax.setText(extractVat.toString())
                var grandTo = totalAmount + vat
                grandTotal.setText(grandTo.toString())

                btn.setOnClickListener {
                    var pay: Double = cash.text.toString().toDouble()
                    var computed: Double = pay - grandTo


                    if (computed < -0.0) {
                        Toast.makeText(context, "Insufficient Cash! ${computed}", Toast.LENGTH_SHORT)
                            .show()
                        totalChange.setText(computed.toString())
                    }
                    else {
                        Toast.makeText(context, "current computed: ${computed}!", Toast.LENGTH_SHORT)
                            .show()

//
//                        var vat = totalAmount * 0.12
//                        var extractVat = "%.2f".format(vat)

                        var extractChange = "%.2f".format(computed)
                        totalChange.setText(extractChange.toString())
//                        totalItems.setText(totalAmount.toString())
//                        tax.setText(extractVat.toString())
//                        var grandTo = totalAmount + vat
//                        grandTotal.setText(grandTo.toString())

                        var request = HttpPosRequest()
                        request.cash = pay
                        request.change = computed
                        request.dispenseBy = "johnbrix17" /*should be pass this*/
                        request.tax = extractVat.toString().toDouble()
                        request.totalItems = totalAmount


                        /*TODO: CREATE LIST REQUEST BY SPECIFIC QTY but same item*/
                        val listRequest: MutableList<HttpPosRequestListItem>? = checkoutAdapter.getList().stream()
                            .map { item ->
                                var listReq = HttpPosRequestListItem()
                                listReq.id = item.productId
                                listReq.itemName = item.itemName
                                listReq.quantity = item.quantity /*TODO FIX THIS IN SEPARATE QTY NOT SAME QTY*/
                                listReq.srpPrice = item.srpPrice
                                listReq
                            }
                            .collect(Collectors.toList())


                        request.itemList = listRequest
                        current.add(response)
                        current.clear()





                        Log.i("ListItemPos: ", listRequest.toString())
                        Log.i("All request from pos: ",request.toString())
                        dialog(context,lifecycleOwner,vModel,request,checkOutDialog,btn)
                    }

                    cash.clearFocus();
                }

            }


        }
    }

    fun dialog(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        vModel: ProductViewModel,
        request: HttpPosRequest,
        checkOutDialog: AlertDialog,
        btn: Button
    ) {

        var dialog: AlertDialog?
        val mBuilder = AlertDialog.Builder(context)
        mBuilder.setCancelable(false)

        mBuilder.setTitle("Confirmation")
        mBuilder.setMessage("Do you want to proceed and print a receipt?")
        mBuilder.setIcon(android.R.drawable.ic_dialog_alert)


        /*TODO: ISSUES: if nakabili kana transaction una okay
           pag 2nd transaction nagpapatong kasama ng transaction na nauna*/
        mBuilder.setPositiveButton("Yes"){dialogInterface, which ->
            btn.isEnabled = false
            Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
            this.totalAmount = 0.0
            checkoutAdapter.freeze()
            vModel.createPos(context,request).observe(lifecycleOwner,Observer{


                checkOutDialog.cancel()
                checkOutDialog.dismiss()
                itemToCart.clear()
                checkoutAdapter.freeze()

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Toast.makeText(
                    applicationContext,
                    "Please wait for refreshing the products!",
                    Toast.LENGTH_SHORT
                ).show()
                productDisplay()

            })
        }

        mBuilder.setNeutralButton("Cancel"){dialogInterface , which ->
            this.totalAmount = 0.0
            btn.isEnabled = true
            checkoutAdapter.notifyDataSetChanged()
            Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }

        dialog = mBuilder.create()
        dialog.show()

    }

    override fun refreshProduct() {

        this.totalAmount = 0.0
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Toast.makeText(
            applicationContext,
            "Please wait for refreshing the products!",
            Toast.LENGTH_SHORT
        ).show()
        productDisplay()

    }
}


interface AddToCart {
    fun onAddToCard(item: Item)
    fun openCheckout(callback: AddToCart, lifecycleOwner: LifecycleOwner, vModel: ProductViewModel)
    fun checkout(
        view: View,
        count: Int,
        response: Item,
        counterValue: CounterView,
        subTotalSpecificItem: TextView,
        priceTextView: TextView,
        isAdded1: ImageView,
        isAdded: Boolean,
        lifecycleOwner: LifecycleOwner,
        vModel: ProductViewModel,
        checkOutDialog: AlertDialog,
        position: Int
    )
    fun refreshProduct()
}
