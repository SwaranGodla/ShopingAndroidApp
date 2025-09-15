package com.example.myapplication.data.repository

import com.example.myapplication.data.model.CartItem
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.util.FakeDataProvider
import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock implementation of CartRepository that uses fake data for testing and development.
 */
@Singleton
class MockCartRepositoryImpl @Inject constructor() : CartRepository {
    
    // In-memory cache of cart items
    private val cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    
    // In-memory cache of products
    private val products = MutableStateFlow(FakeDataProvider.generateProducts(20))
    
    override fun getCartItems(): Flow<List<Product>> {
        return cartItems.map { items ->
            items.mapNotNull { cartItem ->
                products.value.find { it.id == cartItem.productId }
            }
        }
    }
    
    override fun getCartItemCount(): Flow<Int> {
        return cartItems.map { it.sumOf { item -> item.quantity } }
    }
    
    override fun getCartTotal(): Flow<Double> {
        return cartItems.map { items ->
            items.sumOf { cartItem ->
                val product = products.value.find { it.id == cartItem.productId }
                val price = product?.price ?: 0.0
                price * cartItem.quantity
            }
        }
    }
    
    // This method is not in the CartRepository interface, so we're removing it
    
    override suspend fun addToCart(productId: String, quantity: Int): Resource<Boolean> {
        // Simulate network delay
        delay(500)
        
        val product = products.value.find { it.id == productId }
        if (product == null) {
            return Resource.Error("Product not found")
        }
        
        val currentItems = cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.productId == productId }
        
        if (existingItem != null) {
            // Update existing item
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = updatedItem
        } else {
            // Add new item
            val newItem = CartItem(
                id = UUID.randomUUID().toString(),
                productId = productId,
                quantity = quantity
            )
            currentItems.add(newItem)
        }
        
        cartItems.value = currentItems
        
        return Resource.Success(true)
    }
    
    override suspend fun updateCartItemQuantity(productId: String, quantity: Int): Resource<Boolean> {
        // Simulate network delay
        delay(500)
        
        val currentItems = cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.productId == productId }
        
        if (existingItem != null) {
            // Update existing item
            val updatedItem = existingItem.copy(quantity = quantity)
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = updatedItem
            cartItems.value = currentItems
            return Resource.Success(true)
        } else {
            return Resource.Error("Product not found in cart")
        }
    }
    
    override suspend fun removeFromCart(productId: String): Resource<Boolean> {
        // Simulate network delay
        delay(500)
        
        val currentItems = cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.productId == productId }
        
        if (existingItem != null) {
            // Remove item
            currentItems.remove(existingItem)
            cartItems.value = currentItems
            return Resource.Success(true)
        } else {
            return Resource.Error("Product not found in cart")
        }
    }
    
    override suspend fun clearCart(): Resource<Boolean> {
        // Simulate network delay
        delay(500)
        
        cartItems.value = emptyList()
        
        return Resource.Success(true)
    }
    
    // This method is not in the CartRepository interface, so we're removing it
}
