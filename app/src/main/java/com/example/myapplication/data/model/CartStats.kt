package com.example.myapplication.data.model

/**
 * Data class representing statistics about the shopping cart.
 */
data class CartStats(
    val itemCount: Int,
    val subtotal: Double,
    val shipping: Double,
    val tax: Double,
    val total: Double
)
