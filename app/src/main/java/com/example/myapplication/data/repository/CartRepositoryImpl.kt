package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.CartDao
import com.example.myapplication.data.local.dao.ProductDao
import com.example.myapplication.data.local.entity.CartItemEntity
import com.example.myapplication.data.local.entity.toProduct
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.remote.ShoppingApiService
import com.example.myapplication.domain.repository.CartRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of the CartRepository interface.
 * Handles cart operations both locally and with the remote API.
 */
class CartRepositoryImpl @Inject constructor(
    private val api: ShoppingApiService,
    private val cartDao: CartDao,
    private val productDao: ProductDao
) : CartRepository {
    
    override fun getCartItems(): Flow<List<Product>> {
        return cartDao.getCartWithProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }
    
    override fun getCartItemCount(): Flow<Int> {
        return cartDao.getCartItemCount()
    }
    
    override fun getCartTotal(): Flow<Double> {
        return cartDao.getTotalCartPrice().map { it ?: 0.0 }
    }
    
    override suspend fun addToCart(productId: String, quantity: Int): Resource<Boolean> {
        return try {
            // Check if product exists in local database
            val product = productDao.getProductById(productId)
            if (product == null) {
                return Resource.Error("Product not found")
            }
            
            // Check if item is already in cart
            val existingCartItem = cartDao.getCartItemByProductId(productId)
            
            if (existingCartItem != null) {
                // Update quantity if already in cart
                val newQuantity = existingCartItem.quantity + quantity
                cartDao.updateCartItemQuantity(productId, newQuantity)
            } else {
                // Add new item to cart
                cartDao.insertCartItem(
                    CartItemEntity(
                        productId = productId,
                        quantity = quantity
                    )
                )
            }
            
            // Sync with remote API
            try {
                api.addToCart(productId, quantity)
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync cart with remote API")
                // Continue with local operation even if remote sync fails
            }
            
            Resource.Success(true)
        } catch (e: Exception) {
            Timber.e(e, "Error adding item to cart")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun updateCartItemQuantity(productId: String, quantity: Int): Resource<Boolean> {
        return try {
            if (quantity <= 0) {
                removeFromCart(productId)
            } else {
                cartDao.updateCartItemQuantity(productId, quantity)
                
                // Sync with remote API
                try {
                    api.updateCartItem(productId, quantity)
                } catch (e: Exception) {
                    Timber.e(e, "Failed to sync cart update with remote API")
                    // Continue with local operation even if remote sync fails
                }
            }
            
            Resource.Success(true)
        } catch (e: Exception) {
            Timber.e(e, "Error updating cart item quantity")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun removeFromCart(productId: String): Resource<Boolean> {
        return try {
            cartDao.removeCartItem(productId)
            
            // Sync with remote API
            try {
                api.removeFromCart(productId)
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync cart removal with remote API")
                // Continue with local operation even if remote sync fails
            }
            
            Resource.Success(true)
        } catch (e: Exception) {
            Timber.e(e, "Error removing item from cart")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun clearCart(): Resource<Boolean> {
        return try {
            cartDao.clearCart()
            Resource.Success(true)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing cart")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}
