package com.example.myapplication.di

// No BuildConfig import needed
import com.example.myapplication.di.qualifiers.MockRepository
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
 * Module that provides default repository implementations, selecting either real or mock
 * repositories based on build configuration or runtime flags.
 */
@Module
@InstallIn(SingletonComponent::class)
object DefaultRepositoryModule {

    // This flag can be controlled programmatically at runtime if needed
    private var useMockRepositories = true // Default to true for development ease

    /**
     * Sets whether to use mock repositories instead of real ones.
     * This can be called from Application class or test setup.
     */
    fun setUseMockRepositories(useMock: Boolean) {
        useMockRepositories = useMock
    }

    /**
     * Determines whether to use mock repositories based on runtime flags.
     */
    private fun shouldUseMockRepositories(): Boolean {
        return useMockRepositories
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        @RealRepository realRepository: ProductRepository,
        @MockRepository mockRepository: ProductRepository
    ): ProductRepository {
        return if (shouldUseMockRepositories()) {
            mockRepository
        } else {
            realRepository
        }
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        @RealRepository realRepository: AuthRepository,
        @MockRepository mockRepository: AuthRepository
    ): AuthRepository {
        return if (shouldUseMockRepositories()) {
            mockRepository
        } else {
            realRepository
        }
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        @RealRepository realRepository: CartRepository,
        @MockRepository mockRepository: CartRepository
    ): CartRepository {
        return if (shouldUseMockRepositories()) {
            mockRepository
        } else {
            realRepository
        }
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        @RealRepository realRepository: OrderRepository,
        @MockRepository mockRepository: OrderRepository
    ): OrderRepository {
        return if (shouldUseMockRepositories()) {
            mockRepository
        } else {
            realRepository
        }
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        @RealRepository realRepository: PaymentRepository,
        @MockRepository mockRepository: PaymentRepository
    ): PaymentRepository {
        return if (shouldUseMockRepositories()) {
            mockRepository
        } else {
            realRepository
        }
    }
}
