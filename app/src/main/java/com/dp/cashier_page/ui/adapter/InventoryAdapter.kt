package com.dp.cashier_page.ui.adapter

import android.view.*
import android.widget.*
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemList = item.get(position);

        Picasso.get().load(itemList?.itemPicture).into(holder.itemImages)
        holder.itemName.text = itemList.itemName
        holder.srpPrice.text = "₱ ${itemList.srpPrice.toString()}"
        holder.qty.text = "Qty: ${itemList.quantity.toString()}"

    }

    override fun getItemCount(): Int {
        return item.size;
    }


}