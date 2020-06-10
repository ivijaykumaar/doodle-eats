package com.doodleblue.doodleeats.dishes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.doodleblue.doodleeats.R
import com.doodleblue.doodleeats.cart.Cart
import com.doodleblue.doodleeats.db.CartSchema
import com.doodleblue.doodleeats.db.DishSchema
import com.doodleblue.doodleeats.db.DoodleDB
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.bottomsheet_cart.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@Suppress("DEPRECATION")
class DishAdapter(
    private val view: View,
    private var dataList: List<DishSchema>,
    private val activity: Activity

) : CoroutineScope, RecyclerView.Adapter<DishAdapter.Holder>() {
    lateinit var data: DishSchema
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var job: Job

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_dish, parent, false)
        return Holder(itemView)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, @SuppressLint("RecyclerView") position: Int) {
        try {
            job = Job()
            data = dataList[position]
            holder.txtName.text = data.name
            holder.txtInfo.text = data.info
            holder.txtCount.text = data.count.toString()
            holder.txtPrice.text = "₹" + data.price

            if (data.count == 0) {
                holder.btnAdd.visibility = View.VISIBLE
                holder.layoutCount.visibility = View.GONE
            } else {
                holder.btnAdd.visibility = View.GONE
                holder.layoutCount.visibility = View.VISIBLE
            }

            launch {
                val cart =
                    DoodleDB(context = activity.applicationContext).getCart().getCarts()
                if (cart.size == 1) {
                    view.txt_count.text = "(${cart.size} item)"
                } else {
                    view.txt_count.text = "(${cart.size} items)"
                }

                if (cart.isNotEmpty()) {
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }

            holder.btnAdd.setOnClickListener {
                val count = holder.txtCount.text.toString().toInt() + 1
                holder.txtCount.text = count.toString()
                launch {
                    data = dataList[position]
                    val cart = CartSchema(
                        name = data.name,
                        info = data.info,
                        price = data.price,
                        count = count
                    )
                    DoodleDB(context = activity.applicationContext).getCart().insertOrUpdate(cart)
                    val dish = DishSchema(
                        name = data.name,
                        info = data.info,
                        price = data.price,
                        count = count
                    )
                    DoodleDB(context = activity.applicationContext).getDish().insertOrUpdate(dish)

                    val allDishes =
                        DoodleDB(context = activity.applicationContext).getCart().getCarts()
                    Log.e("allDishes", allDishes.toString())
                    if (allDishes.size == 1) {
                        view.txt_count.text = "(${allDishes.size} item)"
                    } else {
                        view.txt_count.text = "(${allDishes.size} items)"
                    }
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                    holder.btnAdd.visibility = View.GONE
                    holder.layoutCount.visibility = View.VISIBLE
                }
            }
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
                        if (allDishes.size == 1) {
                            view.txt_count.text = "(${allDishes.size} item)"
                        } else {
                            view.txt_count.text = "(${allDishes.size} items)"
                        }
                        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetBehavior.state =
                                BottomSheetBehavior.STATE_EXPANDED
                        }
                        if (allDishes.isEmpty()) {
                            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            }
                        }
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
                        holder.btnAdd.visibility = View.VISIBLE
                        holder.layoutCount.visibility = View.GONE
                    }

                    val allDishes =
                        DoodleDB(context = activity.applicationContext).getCart().getCarts()
                    Log.e("allDishes", allDishes.toString())
                    if (allDishes.size == 1) {
                        view.txt_count.text = "(${allDishes.size} item)"
                    } else {
                        view.txt_count.text = "(${allDishes.size} items)"
                    }
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                    if (allDishes.isEmpty()) {
                        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                }
            }

            bottomSheetBehavior = BottomSheetBehavior.from(view.bottomsheet_cart)
            bottomSheetBehavior.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {

                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {

                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> {

                        }
                        BottomSheetBehavior.STATE_SETTLING -> {

                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            TODO()
                        }
                    }
                }
            })

            view.lay_viewcart.setOnClickListener {
                Handler().postDelayed({
                    val intent = Intent(activity, Cart::class.java)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                }, 100)
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
        return dataList.size
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: MaterialTextView = view.findViewById(R.id.txt_name)
        var txtInfo: MaterialTextView = view.findViewById(R.id.txt_information)
        var txtPrice: MaterialTextView = view.findViewById(R.id.txt_price)
        var imgMinus: AppCompatImageView = view.findViewById(R.id.img_minus)
        var txtCount: MaterialTextView = view.findViewById(R.id.txt_count)
        var imgPlus: AppCompatImageView = view.findViewById(R.id.img_plus)
        var btnAdd: MaterialTextView = view.findViewById(R.id.txt_add)
        var layoutCount: LinearLayoutCompat = view.findViewById(R.id.layout_count)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}
