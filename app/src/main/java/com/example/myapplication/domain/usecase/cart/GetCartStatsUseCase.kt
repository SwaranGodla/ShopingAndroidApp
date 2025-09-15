package com.example.myapplication.domain.usecase.cart

import com.example.myapplication.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving cart statistics (item count and total price).
 */
class GetCartStatsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Get the total number of items in the cart.
     *
     * @return Flow of cart item count.
     */
    fun getCartItemCount(): Flow<Int> {
        return cartRepository.getCartItemCount()
    }
    
    /**
     * Get the total price of all items in the cart.
     *
     * @return Flow of cart total price.
     */
    fun getCartTotal(): Flow<Double> {
        return cartRepository.getCartTotal()
    }
}
