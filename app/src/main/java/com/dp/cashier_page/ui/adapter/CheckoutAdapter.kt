package com.dp.cashier_page.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.dp.cashier_page.R
import com.dp.cashier_page.domain.response.product.Item
import com.dp.cashier_page.ui.activities.AddToCart
import com.dp.cashier_page.ui.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import de.starkling.shoppingcart.widget.CounterView

class CheckoutAdapter(
    var item: List<Item>,
    val callback: AddToCart,
    val view: View,
    val lifecycleOwner: LifecycleOwner,
    val vModel: ProductViewModel,
    var checkOutDialog: AlertDialog
) : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {

    private var listData: MutableList<Item> = item as MutableList<Item>

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImages = itemView.findViewById(R.id.productImage) as ImageView
        var itemName = itemView.findViewById(R.id.productNameTextView) as TextView
        var srpPrice = itemView.findViewById(R.id.priceTextView) as TextView
        var counterView = itemView.findViewById(R.id.counterView) as CounterView
        var subTotalSpecificItem = itemView.findViewById(R.id.subTotalSpecificItem) as TextView
        var priceTextView = itemView.findViewById(R.id.priceTextView) as TextView
        var exit = itemView.findViewById(R.id.exit) as ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_list, parent, false)
        return ViewHolder(v)
    }

    fun getList(): List<Item> = listData

    lateinit var vh :ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        var itemList = listData.get(position)

        holder.counterView.addCounterValueChangeListener(object : CounterView.CounterValueChangeListener {
            override fun onValueDelete(count: Int) {
                itemList.quantity = count

                callback.checkout(
                    view,
                    count,
                    itemList,
                    holder.counterView,
                    holder.subTotalSpecificItem,
                    holder.priceTextView,
                    holder.exit,
                    false,
                    lifecycleOwner,
                    vModel,
                    checkOutDialog,
                    position
                )
                notifyDataSetChanged()
            }

            override fun onValueAdd(count: Int) {

                itemList.quantity = count

                callback.checkout(
                    view,
                    count,
                    itemList,
                    holder.counterView,
                    holder.subTotalSpecificItem,
                    holder.priceTextView,
                    holder.exit,
                    true,
                    lifecycleOwner,
                    vModel,
                    checkOutDialog,
                    position
                )
            notifyDataSetChanged()
            }

        })

        Picasso.get().load(itemList.itemPicture).into(holder.itemImages)
        vh = holder
        holder.exit.setOnClickListener {

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position)

            deleteItem(position)
        }

        holder.itemName.text = itemList.itemName
        holder.srpPrice.text = "₱ ${itemList.srpPrice.toString()}"

    }
    fun deleteItem(index: Int){

        listData.removeAt(index)

        if(listData.isEmpty()){
            /*CANCEL DIALOG DUE NO CARTS */

            Toast.makeText(view.context, "Cannot continue due no added items in cart!", Toast.LENGTH_SHORT)
                .show()

            listData.clear()

            checkOutDialog.cancel()
            checkOutDialog.dismiss()
            callback.refreshProduct()
            notifyDataSetChanged()
        }
        notifyDataSetChanged()
    }
    fun freeze(){
        vh.exit.isEnabled = false
        vh.exit.isClickable = false
    }


    override fun getItemCount(): Int {
        return item.size;
    }
}