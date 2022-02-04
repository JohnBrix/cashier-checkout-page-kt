package com.dp.cashier_page

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*TODO: THIS IS LIST NOT AN ACTIVITY LAYOUT, DUE FOR SOURCE CODE PURPOSE YOU PUT THAT AS DISPLAY*/
        setContentView(R.layout.item_product_list)
        supportActionBar?.title = "Products"

        var cash = findViewById(R.id.cash) as TextInputEditText
        var cashLayout = findViewById(R.id.cashLayout) as TextInputLayout
        var screen = findViewById(R.id.screen) as LinearLayout


        screen.setOnClickListener{
            cash.clearFocus();
        }
    }


}
