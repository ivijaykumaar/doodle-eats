package com.doodleblue.doodleeats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doodleblue.doodleeats.general.Constants.DOODLE

@Database(
    entities = [CartSchema::class, DishSchema::class],
    version = 1
)
abstract class DoodleDB : RoomDatabase() {

    abstract fun getCart(): CartDao
    abstract fun getDish(): DishDao

    companion object {
        @Volatile
        private var instance: DoodleDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, DoodleDB::class.java, DOODLE)
                .allowMainThreadQueries()
                .build()
    }
}