package com.example.myapplication.di

import com.example.myapplication.data.repository.MockAuthRepositoryImpl
import com.example.myapplication.data.repository.MockCartRepositoryImpl
import com.example.myapplication.data.repository.MockOrderRepositoryImpl
import com.example.myapplication.data.repository.MockPaymentRepositoryImpl
import com.example.myapplication.data.repository.MockProductRepositoryImpl
import com.example.myapplication.di.qualifiers.MockRepository
import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.repository.OrderRepository
import com.example.myapplication.domain.repository.PaymentRepository
import com.example.myapplication.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing mock repository implementations for development and testing.
 * This module replaces the real RepositoryModule when mock data is needed.
 *
 * NOTE: Temporarily commented out to resolve duplicate binding issues
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MockRepositoryModule {

    /**
     * Provides the mock ProductRepository implementation.
     */
    @Binds
    @Singleton
    @MockRepository
    abstract fun bindProductRepository(
        mockProductRepositoryImpl: MockProductRepositoryImpl
    ): ProductRepository

    /**
     * Provides the mock AuthRepository implementation.
     */
    @Binds
    @Singleton
    @MockRepository
    abstract fun bindAuthRepository(
        mockAuthRepositoryImpl: MockAuthRepositoryImpl
    ): AuthRepository

    /**
     * Provides the mock CartRepository implementation.
     */
    @Binds
    @Singleton
    @MockRepository
    abstract fun bindCartRepository(
        mockCartRepositoryImpl: MockCartRepositoryImpl
    ): CartRepository

    /**
     * Provides the mock OrderRepository implementation.
     */
    @Binds
    @Singleton
    @MockRepository
    abstract fun bindOrderRepository(
        mockOrderRepositoryImpl: MockOrderRepositoryImpl
    ): OrderRepository

    /**
     * Provides the mock PaymentRepository implementation.
     */
    @Binds
    @Singleton
    @MockRepository
    abstract fun bindPaymentRepository(
        mockPaymentRepositoryImpl: MockPaymentRepositoryImpl
    ): PaymentRepository
}
