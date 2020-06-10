package com.doodleblue.doodleeats.cart

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.doodleblue.doodleeats.R
import com.doodleblue.doodleeats.db.CartSchema
import com.doodleblue.doodleeats.db.DoodleDB
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Suppress("DEPRECATION")
class CartAdapter(
    private val flag: Boolean,
    private var dataList: List<CartSchema>,
    private val activity: Activity

) : CoroutineScope, RecyclerView.Adapter<CartAdapter.Holder>() {
    lateinit var data: CartSchema
    private lateinit var job: Job

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_mycart, parent, false)
        return Holder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        try {
            job = Job()
            data = dataList[position]
            holder.txtName.text = data.name
            holder.txtInfo.text = data.info
            holder.txtPrice.text = "₹" + data.price
            holder.txtCount.text = data.count.toString()

            holder.imgPlus.setOnClickListener {
                val count = holder.txtCount.text.toString().toInt() + 1
                if (count <= 20) {
                    holder.txtCount.text = count.toString()
                    launch {
                        data = dataList[position]
                        DoodleDB(context = activity.applicationContext).getCart()
                            .addCartByName(name = data.name)
                        DoodleDB(context = activity.applicationContext).getDish()
                            .addDishByName(name = data.name)
                        val allDishes = DoodleDB(context = activity.applicationContext).getCart()
                            .getCarts()
                        Log.e("allDishes", allDishes.toString())
                        activity.finish()
                        activity.overridePendingTransition(0, 0)
                        activity.startActivity(activity.intent)
                        activity.overridePendingTransition(0, 0)
                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Cart is full! (Maximum 20 items are allowed)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            holder.imgMinus.setOnClickListener {
                val count = holder.txtCount.text.toString().toInt() - 1
                holder.txtCount.text = count.toString()

                launch {
                    data = dataList[position]
                    DoodleDB(context = activity.applicationContext).getCart()
                        .removeCartByName(name = data.name)
                    DoodleDB(context = activity.applicationContext).getDish()
                        .removeDishByName(name = data.name)
                    if (count == 0) {
                        DoodleDB(context = activity.applicationContext).getCart()
                            .deleteByName(data.name)
                    }

                    val allDishes =
                        DoodleDB(context = activity.applicationContext).getCart().getCarts()
                    Log.e("allDishes", allDishes.toString())
                    activity.finish()
                    activity.overridePendingTransition(0, 0)
                    activity.startActivity(activity.intent)
                    activity.overridePendingTransition(0, 0)
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            e.printStackTrace()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return if (flag) {
            return dataList.size
        } else {
            dataList.size
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: MaterialTextView = view.findViewById(R.id.txt_name)
        var txtInfo: MaterialTextView = view.findViewById(R.id.txt_information)
        var txtPrice: MaterialTextView = view.findViewById(R.id.txt_price)
        var imgMinus: AppCompatImageView = view.findViewById(R.id.img_minus)
        var txtCount: MaterialTextView = view.findViewById(R.id.txt_count)
        var imgPlus: AppCompatImageView = view.findViewById(R.id.img_plus)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}
