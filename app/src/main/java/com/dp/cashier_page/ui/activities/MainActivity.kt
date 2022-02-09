package com.dp.cashier_page.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dp.cashier_page.R
import com.dp.cashier_page.domain.response.product.Item
import com.google.android.material.textfield.TextInputEditText
import de.starkling.shoppingcart.widget.CounterView

class MainActivity : AppCompatActivity() {

    lateinit var cash: TextInputEditText
    lateinit var btn: Button
    lateinit var productNameTextView: TextView
    lateinit var priceTextView: TextView
    lateinit var totalSpecificItems: TextView
    lateinit var totalItems: TextView
    lateinit var tax: TextView
    lateinit var grandTotal: TextView

    lateinit var counterView: CounterView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*TODO: THIS IS LIST NOT AN ACTIVITY LAYOUT, DUE FOR SOURCE CODE PURPOSE YOU PUT THAT AS DISPLAY*/
        setContentView(R.layout.item_product_list)
        supportActionBar?.title = "Products"

        cash = findViewById(R.id.cash) as TextInputEditText
        btn = findViewById(R.id.checkOut) as Button
        productNameTextView = findViewById(R.id.productNameTextView) as TextView
        productNameTextView.setText("Raider Headlight")
        priceTextView = findViewById(R.id.priceTextView) as TextView
        totalSpecificItems = findViewById(R.id.subTotalSpecificItem) as TextView
        totalItems = findViewById(R.id.totalItems) as TextView
        tax = findViewById(R.id.tax) as TextView

        grandTotal = findViewById(R.id.grandTotal) as TextView

        counterView = findViewById(R.id.counterView) as CounterView

        /*SAMPLE RESPONSE FROM BACKEND*/
        var response = Item()
        response.quantity = 5
        response.srpPrice = 1000.0

        counterView.addCounterValueChangeListener(object : CounterView.CounterValueChangeListener {
            override fun onValueDelete(count: Int) {
                computation(count,response)
            }

            override fun onValueAdd(count: Int) {
                computation(count, response)
            }

        })


    }

    private fun computation(
        count: Int,
        response: Item
    ) {

        var price: Double = response.srpPrice!!
        var qtyToPriceTotal = count * price


        if (count > response.quantity!!) {
            Toast.makeText(applicationContext, "${count}", Toast.LENGTH_SHORT).show()
            counterView.counterValue = response.quantity!! //Stoping the count

        } else {
            totalSpecificItems.text = qtyToPriceTotal.toString()
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
                    Toast.makeText(applicationContext, "Insufficient Cash!", Toast.LENGTH_SHORT)
                        .show()
                }

                //CREATE POS REQUEST  TO BACKEND

                cash.clearFocus();
            }
        }
    }


}
