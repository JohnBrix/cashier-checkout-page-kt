package com.dp.cashier_page

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dp.cashier_page.data.HttpProductListItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.starkling.shoppingcart.widget.CounterView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*TODO: THIS IS LIST NOT AN ACTIVITY LAYOUT, DUE FOR SOURCE CODE PURPOSE YOU PUT THAT AS DISPLAY*/
        setContentView(R.layout.item_product_list)
        supportActionBar?.title = "Products"

        var cash = findViewById(R.id.cash) as TextInputEditText
        var cashLayout = findViewById(R.id.cashLayout) as TextInputLayout
        var btn = findViewById(R.id.checkOut) as Button
        var productNameTextView = findViewById(R.id.productNameTextView) as TextView
        productNameTextView.setText("Raider Headlight")
        var priceTextView = findViewById(R.id.priceTextView) as TextView
        var totalSpecificItems = findViewById(R.id.subTotalSpecificItem) as TextView

        var counterView = findViewById(R.id.counterView) as CounterView

        /*SAMPLE RESPONSE FROM BACKEND*/
        var response = HttpProductListItem()
        response.quantity = 5
        response.srpPrice = 500.0


        counterView.addCounterValueChangeListener(object :CounterView.CounterValueChangeListener{
            override fun onValueDelete(count: Int) {
                var price: Double = response.srpPrice!!
                var qtyToPriceTotal = count * price
                totalSpecificItems.text = qtyToPriceTotal.toString()
            }

            override fun onValueAdd(count: Int) {
                /*TODO FIX THE BUGS INCREMENT QTY BUT GOT EXTRA QTY*/

                var price: Double = response.srpPrice!!
                var qtyToPriceTotal = count * price

                if (count >= response.quantity!!){
                    Toast.makeText(applicationContext, "counterValue", Toast.LENGTH_SHORT).show()
                    counterView.counterValue = response.quantity!!
                }

                totalSpecificItems.text = qtyToPriceTotal.toString()

                priceTextView.setText(response.srpPrice.toString()) /*DISPLAY UI FROM RESPONSE*/

                var totalChange = findViewById(R.id.totalChange) as TextView
                btn.setOnClickListener {

                    /*AUTO COMPUTE*/

                    var pay: Double = cash.text.toString().toDouble()


                    var computed: Double = pay - qtyToPriceTotal

                    totalChange.setText(computed.toString())

                    if (computed < -0.0) {
                        Toast.makeText(applicationContext, "Insufficient Cash!", Toast.LENGTH_SHORT).show()
                    }

                    cash.clearFocus();
                }
            }

        })


    }


}
