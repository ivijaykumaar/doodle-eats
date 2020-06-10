package com.doodleblue.doodleeats.dishes

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.doodleblue.doodleeats.R
import com.doodleblue.doodleeats.db.DoodleDB
import com.doodleblue.doodleeats.general.CoroutineActivity
import kotlinx.android.synthetic.main.act_home.*
import kotlinx.android.synthetic.main.bottomsheet_cart.*
import kotlinx.coroutines.launch


class Home : CoroutineActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)
        im_back.setOnClickListener {
            onBackPressed()
        }
        loadDishes()
    }

    override fun onRestart() {
        super.onRestart()
        loadDishes()
    }

    private fun loadDishes() {
        try {
            launch {
                val listOfDish = DoodleDB(context = applicationContext).getDish().getAllDishes()
                if (listOfDish.isNotEmpty()) {
                    view_dish?.apply {
                        view_dish?.layoutManager = LinearLayoutManager(this@Home)
                        view_dish?.adapter =
                            DishAdapter(
                                view = bottomsheet_cart,
                                dataList = listOfDish,
                                activity = this@Home
                            )
                    }
                }
            }
        } catch (exception: Exception) {
            Log.e("Exception", exception.toString())
        }
    }
}