package com.doodleblue.doodleeats.general

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.doodleblue.doodleeats.R
import com.doodleblue.doodleeats.db.DishSchema
import com.doodleblue.doodleeats.db.DoodleDB
import com.doodleblue.doodleeats.dishes.Home
import kotlinx.coroutines.launch


class Splash : CoroutineActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        firstTime()
        Handler().postDelayed({
            startActivity(Intent(this@Splash, Home::class.java))
            this@Splash.overridePendingTransition(
                R.anim.fade_in,
                R.anim.fade_out
            )
            finish()
        }, 2000)
        super.onResume()
    }

    private fun firstTime() {
        val firstTime = getData("firstTime", applicationContext).toString()
        if (firstTime.isEmpty()) {
            launch {
                saveData("firstTime", "true", applicationContext).toString()
                val listOfDishes = listOf(
                    DishSchema(
                        name = "Kalu Fry",
                        info = "Briyani zones special",
                        price = "250",
                        count = 0
                    ),
                    DishSchema(
                        name = "Bhindi Fry (Vepudu)",
                        info = "Briyani zones special",
                        price = "120",
                        count = 0
                    ),
                    DishSchema(
                        name = "Veg 65",
                        info = "Briyani zones special",
                        price = "190",
                        count = 0
                    )
                )
                for (item in listOfDishes) {
                    DoodleDB(context = applicationContext).getDish().addDish(item)
                }
            }
        }
    }
}