package com.example.myapplication.domain.usecase.cart

import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for adding a product to the cart.
 */
class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Add a product to the cart.
     *
     * @param productId The ID of the product to add.
     * @param quantity The quantity to add (default: 1).
     * @return Resource indicating success or failure.
     */
    suspend operator fun invoke(productId: String, quantity: Int = 1): Resource<Boolean> {
        if (productId.isBlank()) {
            return Resource.Error("Product ID cannot be empty")
        }
        
        if (quantity <= 0) {
            return Resource.Error("Quantity must be greater than zero")
        }
        
        return cartRepository.addToCart(productId, quantity)
    }
}
