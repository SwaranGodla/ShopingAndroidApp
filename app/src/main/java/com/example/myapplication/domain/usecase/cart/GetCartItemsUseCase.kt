package com.example.myapplication.domain.usecase.cart

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all items in the cart.
 */
class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    /**
     * Get all items in the cart with their product details.
     *
     * @return Flow of products in the cart.
     */
    operator fun invoke(): Flow<List<Product>> {
        return cartRepository.getCartItems()
    }
}
