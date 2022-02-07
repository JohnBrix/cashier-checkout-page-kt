package com.dp.cashier_page.ui.adapter

import android.app.AlertDialog
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dp.cashier_page.R
import com.dp.cashier_page.domain.Item
import com.squareup.picasso.Picasso


class InventoryAdapter(
    val item: List<Item>
) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImages = itemView.findViewById(R.id.imageR) as ImageView
        var itemName = itemView.findViewById(R.id.productItem) as TextView
        var qty = itemView.findViewById(R.id.qty) as TextView
        var srpPrice = itemView.findViewById(R.id.price) as TextView
        var add = itemView.findViewById(R.id.addToCart) as Button
        var fab = itemView.findViewById(R.id.fab) as Button


        fun addToCartToCheckout(item: Item) {


            itemView.apply {
                fab.setOnClickListener {
                    val view = LayoutInflater.from(context)
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

                    val window: Window? = dialog.getWindow()
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
                          /*  GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)*/
                        dashboardRecycleView?.layoutManager = recyclerView.layoutManager

                        var listItem = ArrayList<Item>()
                        listItem.add(item)

                        recyclerView.adapter = CheckoutAdapter(listItem)




                    }



                    }

                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemList = item.get(position)
        
        
        holder.add.setOnClickListener{
            holder.addToCartToCheckout(itemList)/*ADDED TO CART*/
        }
        

        Picasso.get().load(itemList?.itemPicture).into(holder.itemImages)
        holder.itemName.text = itemList.itemName
        holder.srpPrice.text = "₱ ${itemList.srpPrice.toString()}"
        holder.qty.text = "Qty: ${itemList.quantity.toString()}"

    }

    override fun getItemCount(): Int {
        return item.size;
    }


}