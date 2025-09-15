package com.example.myapplication.di

import com.example.myapplication.data.local.dao.CartDao
import com.example.myapplication.data.local.dao.ProductDao
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.remote.PaymentService
import com.example.myapplication.data.remote.ShoppingApiService
import com.example.myapplication.data.repository.AuthRepositoryImpl
import com.example.myapplication.data.repository.CartRepositoryImpl
import com.example.myapplication.data.repository.OrderRepositoryImpl
import com.example.myapplication.data.repository.PaymentRepositoryImpl
import com.example.myapplication.data.repository.ProductRepositoryImpl
import com.example.myapplication.di.qualifiers.RealRepository
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.repository.OrderRepository
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    /**
     * Provides the ProductRepository implementation.
     */
    @Provides
    @Singleton
    @RealRepository
    fun provideProductRepository(
        api: ShoppingApiService,
        productDao: ProductDao
    ): ProductRepository {
        return ProductRepositoryImpl(api, productDao)
    }
    
    /**
     * Provides the AuthRepository implementation.
     */
    @Provides
    @Singleton
    @RealRepository
    fun provideAuthRepository(
        api: ShoppingApiService,
        userDao: UserDao
    ): AuthRepository {
        return AuthRepositoryImpl(api, userDao)
    }
    
    /**
     * Provides the CartRepository implementation.
     */
    @Provides
    @Singleton
    @RealRepository
    fun provideCartRepository(
        api: ShoppingApiService,
        cartDao: CartDao,
        productDao: ProductDao
    ): CartRepository {
        return CartRepositoryImpl(api, cartDao, productDao)
    }
    
    /**
     * Provides the PaymentRepository implementation.
     */
    @Provides
    @Singleton
    @RealRepository
    fun providePaymentRepository(paymentService: PaymentService): PaymentRepository {
        return PaymentRepositoryImpl(paymentService)
    }
    
    /**
     * Provides the OrderRepository implementation.
     */
    @Provides
    @Singleton
    @RealRepository
    fun provideOrderRepository(api: ShoppingApiService): OrderRepository {
        return OrderRepositoryImpl(api)
    }
}
