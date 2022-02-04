package com.dp.cashier_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*TODO: THIS IS LIST NOT AN ACTIVITY LAYOUT, DUE FOR SOURCE CODE PURPOSE YOU PUT THAT AS DISPLAY*/
        setContentView(R.layout.item_product_list)
        supportActionBar?.title = "Products"


    }


}
