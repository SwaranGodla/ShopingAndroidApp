package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for cart-related operations.
 */
interface CartRepository {
    
    /**
     * Get all items in the cart with their product details.
     *
     * @return Flow of products in the cart.
     */
    fun getCartItems(): Flow<List<Product>>
    
    /**
     * Get the total number of items in the cart.
     *
     * @return Flow of cart item count.
     */
    fun getCartItemCount(): Flow<Int>
    
    /**
     * Get the total price of all items in the cart.
     *
     * @return Flow of cart total price.
     */
    fun getCartTotal(): Flow<Double>
    
    /**
     * Add a product to the cart.
     *
     * @param productId The ID of the product to add.
     * @param quantity The quantity to add (default: 1).
     * @return Resource indicating success or failure.
     */
    suspend fun addToCart(productId: String, quantity: Int = 1): Resource<Boolean>
    
    /**
     * Update the quantity of a product in the cart.
     *
     * @param productId The ID of the product to update.
     * @param quantity The new quantity.
     * @return Resource indicating success or failure.
     */
    suspend fun updateCartItemQuantity(productId: String, quantity: Int): Resource<Boolean>
    
    /**
     * Remove a product from the cart.
     *
     * @param productId The ID of the product to remove.
     * @return Resource indicating success or failure.
     */
    suspend fun removeFromCart(productId: String): Resource<Boolean>
    
    /**
     * Clear all items from the cart.
     *
     * @return Resource indicating success or failure.
     */
    suspend fun clearCart(): Resource<Boolean>
}
