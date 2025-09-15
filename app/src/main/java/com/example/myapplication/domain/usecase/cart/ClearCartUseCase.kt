package com.example.myapplication.domain.usecase.cart

import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for clearing all items from the cart.
 */
class ClearCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Clear all items from the cart.
     *
     * @return Resource indicating success or failure.
     */
    suspend operator fun invoke(): Resource<Boolean> {
        return cartRepository.clearCart()
    }
}
