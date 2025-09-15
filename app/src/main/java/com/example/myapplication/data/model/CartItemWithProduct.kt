package com.example.myapplication.data.model

/**
 * Data class representing a cart item with its associated product details.
 */
data class CartItemWithProduct(
    val cartItem: CartItem,
    val product: Product
)
