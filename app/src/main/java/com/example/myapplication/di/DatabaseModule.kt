package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.ShoppingDatabase
import com.example.myapplication.data.local.dao.CartDao
import com.example.myapplication.data.local.dao.ProductDao
import com.example.myapplication.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides the Room database instance.
     */
    @Provides
    @Singleton
    fun provideShoppingDatabase(
        @ApplicationContext context: Context
    ): ShoppingDatabase {
        return Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            ShoppingDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    /**
     * Provides the ProductDao instance.
     */
    @Provides
    @Singleton
    fun provideProductDao(database: ShoppingDatabase): ProductDao {
        return database.productDao()
    }
    
    /**
     * Provides the CartDao instance.
     */
    @Provides
    @Singleton
    fun provideCartDao(database: ShoppingDatabase): CartDao {
        return database.cartDao()
    }
    
    /**
     * Provides the UserDao instance.
     */
    @Provides
    @Singleton
    fun provideUserDao(database: ShoppingDatabase): UserDao {
        return database.userDao()
    }
}
