package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.local.dao.CartDao
import com.example.myapplication.data.local.dao.ProductDao
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entity.CartItemEntity
import com.example.myapplication.data.local.entity.ProductEntity
import com.example.myapplication.data.local.entity.UserEntity
import com.example.myapplication.data.local.util.Converters

/**
 * Room database for the shopping application.
 * Contains tables for products, cart items, and user information.
 */
@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShoppingDatabase : RoomDatabase() {
    
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao
    
    companion object {
        const val DATABASE_NAME = "shopping_database"
    }
}
