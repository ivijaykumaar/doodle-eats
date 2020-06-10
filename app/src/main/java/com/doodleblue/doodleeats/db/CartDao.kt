package com.doodleblue.doodleeats.db

import androidx.room.*

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(cartSchema: CartSchema)

    @Query("SELECT * from Cart WHERE name= :name")
    suspend fun getCartByName(name: String): List<CartSchema>

    @Query("UPDATE Cart SET count = count + 1 WHERE name = :name")
    suspend fun addCartByName(name: String)

    @Query("UPDATE Cart SET count = count - 1 WHERE name = :name")
    suspend fun removeCartByName(name: String)

    suspend fun insertOrUpdate(item: CartSchema) {
        val itemsFromDB = getCartByName(item.name)
        if (itemsFromDB.isEmpty()) {
            addCart(item)
        } else {
            addCartByName(item.name)
        }
    }

    @Query("DELETE FROM Cart WHERE name = :name")
    suspend fun deleteByName(name: String)

    @Update
    suspend fun updateCart(cartSchema: CartSchema)

    @Query("SELECT * FROM Cart")
    suspend fun getCarts(): List<CartSchema>

    @Query("DELETE FROM Cart")
    suspend fun deleteAll()

}