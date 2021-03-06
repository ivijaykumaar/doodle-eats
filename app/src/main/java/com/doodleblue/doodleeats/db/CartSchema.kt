package com.doodleblue.doodleeats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// cart table
@Entity(
    tableName = "cart",
    indices = [Index(value = ["name"], unique = true)]
)
data class CartSchema(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "info")
    var info: String,
    @ColumnInfo(name = "count")
    var count: Int,
    @ColumnInfo(name = "price")
    var price: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}