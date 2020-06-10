package com.doodleblue.doodleeats.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.doodleblue.doodleeats.R
import com.doodleblue.doodleeats.db.DoodleDB
import com.doodleblue.doodleeats.general.CoroutineActivity
import kotlinx.android.synthetic.main.act_cart.*
import kotlinx.android.synthetic.main.act_home.view_dish
import kotlinx.coroutines.launch

class Cart : CoroutineActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_cart)

        im_back.setOnClickListener {
            onBackPressed()
        }
        loadCart()
    }

    @SuppressLint("SetTextI18n")
    fun loadCart() {
        launch {
            val listOfCart = DoodleDB(context = applicationContext).getCart().getCarts()
            if (listOfCart.isNotEmpty()) {
                if (listOfCart.size <= 2) {
                    btn_more.visibility = View.GONE
                } else {
                    btn_more.visibility = View.VISIBLE
                }
                view_dish?.apply {
                    view_dish?.layoutManager = LinearLayoutManager(this@Cart)
                    view_dish?.adapter =
                        CartAdapter(
                            flag = true,
                            dataList = listOfCart,
                            activity = this@Cart
                        )
                }
            }

            btn_more.setOnClickListener {
                view_dish?.apply {
                    view_dish?.layoutManager = LinearLayoutManager(this@Cart)
                    view_dish?.adapter =
                        CartAdapter(
                            flag = false,
                            dataList = listOfCart,
                            activity = this@Cart
                        )
                }
                btn_more.visibility = View.GONE
            }

            var total = 0
            for (item in listOfCart) {
                total += item.count * item.price.toInt()
            }
            txt_total.text = "₹$total"
        }
    }
}