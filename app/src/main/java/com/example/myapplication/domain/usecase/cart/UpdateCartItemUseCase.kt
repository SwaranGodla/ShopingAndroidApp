package com.example.myapplication.domain.usecase.cart

import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for updating the quantity of a product in the cart.
 */
class UpdateCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Update the quantity of a product in the cart.
     *
     * @param productId The ID of the product to update.
     * @param quantity The new quantity.
     * @return Resource indicating success or failure.
     */
    suspend operator fun invoke(productId: String, quantity: Int): Resource<Boolean> {
        if (productId.isBlank()) {
            return Resource.Error("Product ID cannot be empty")
        }
        
        // If quantity is 0 or negative, remove the item from cart
        if (quantity <= 0) {
            return cartRepository.removeFromCart(productId)
        }
        
        return cartRepository.updateCartItemQuantity(productId, quantity)
    }
}
