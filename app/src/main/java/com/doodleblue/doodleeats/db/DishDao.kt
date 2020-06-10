package com.doodleblue.doodleeats.db

import androidx.room.*

@Dao
interface DishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDish(dishSchema: DishSchema)

    @Query("SELECT * from Dish WHERE name= :name")
    suspend fun getDishByName(name: String): List<DishSchema>

    @Query("UPDATE Dish SET count = count + 1 WHERE name = :name")
    suspend fun addDishByName(name: String)

    @Query("UPDATE Dish SET count = count - 1 WHERE name = :name")
    suspend fun removeDishByName(name: String)

    suspend fun insertOrUpdate(item: DishSchema) {
        val itemsFromDB = getDishByName(item.name)
        if (itemsFromDB.isEmpty()) {
            addDish(item)
        } else {
            addDishByName(item.name)
        }
    }

    @Query("DELETE FROM Dish WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Update
    suspend fun updateDish(dishSchema: DishSchema)

    @Query("SELECT * FROM Dish")
    suspend fun getAllDishes(): List<DishSchema>

    @Query("DELETE FROM Dish")
    suspend fun deleteAll()

}