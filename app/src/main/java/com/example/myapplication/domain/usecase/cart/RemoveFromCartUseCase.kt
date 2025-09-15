package com.example.myapplication.domain.usecase.cart

import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for removing a product from the cart.
 */
class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Remove a product from the cart.
     *
     * @param productId The ID of the product to remove.
     * @return Resource indicating success or failure.
     */
    suspend operator fun invoke(productId: String): Resource<Boolean> {
        if (productId.isBlank()) {
            return Resource.Error("Product ID cannot be empty")
        }
        
        return cartRepository.removeFromCart(productId)
    }
}
