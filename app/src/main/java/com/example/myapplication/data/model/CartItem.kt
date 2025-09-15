package com.example.myapplication.data.model

/**
 * Data class representing an item in the shopping cart.
 */
data class CartItem(
    val id: String,
    val productId: String,
    val quantity: Int
)
