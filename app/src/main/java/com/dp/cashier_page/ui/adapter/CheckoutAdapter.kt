package com.dp.cashier_page.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dp.cashier_page.R
import com.dp.cashier_page.domain.Item
import com.dp.cashier_page.ui.activities.AddToCart
import com.squareup.picasso.Picasso
import de.starkling.shoppingcart.widget.CounterView

class CheckoutAdapter(val item: List<Item>, val callback: AddToCart, val view: View) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImages = itemView.findViewById(R.id.productImage) as ImageView
        var itemName = itemView.findViewById(R.id.productNameTextView) as TextView
        var srpPrice = itemView.findViewById(R.id.priceTextView) as TextView
        var counterView = itemView.findViewById(R.id.counterView) as CounterView
        var subTotalSpecificItem = itemView.findViewById(R.id.subTotalSpecificItem) as TextView
        var priceTextView = itemView.findViewById(R.id.priceTextView) as TextView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.ViewHolder, position: Int) {
        var itemList = item.get(position)

        println("ADDED ITEMS: "+itemList)

        holder.counterView.addCounterValueChangeListener(object : CounterView.CounterValueChangeListener {
            override fun onValueDelete(count: Int) {
                callback.checkout(
                    view,
                    count,
                    itemList,
                    holder.counterView,
                    holder.subTotalSpecificItem,
                    holder.priceTextView,
                    false
                )
            }

            override fun onValueAdd(count: Int) {
                callback.checkout(
                    view,
                    count,
                    itemList,
                    holder.counterView,
                    holder.subTotalSpecificItem,
                    holder.priceTextView,
                    true
                )
            }

        })

        Picasso.get().load(itemList.itemPicture).into(holder.itemImages)
        holder.itemName.text = itemList.itemName
        holder.srpPrice.text = "₱ ${itemList.srpPrice.toString()}"

    }

    override fun getItemCount(): Int {
        return item.size;
    }
}