package com.example.myapplication.data.model

import java.util.Date

/**
 * Data class representing an order in the shopping application.
 */
data class Order(
    val id: String,
    val userId: String,
    val orderDate: Date,
    val items: List<OrderItem>,
    val subtotal: Double,
    val tax: Double,
    val shippingCost: Double,
    val totalAmount: Double,
    val total: Double,
    val status: OrderStatus,
    val paymentMethod: String,
    val createdAt: String,
    val shippingAddress: Address,
    val billingAddress: Address? = null,
    val trackingNumber: String? = null,
    val estimatedDeliveryDate: Date? = null,
    val notes: String? = null,
    val updatedAt: String? = null
)

/**
 * Data class representing an item in an order.
 */
data class OrderItem(
    val id: String,
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val totalPrice: Double,
    val imageUrl: String? = null,
    val productImage: String? = null
)

/**
 * Data class representing a shipping or billing address.
 */
data class Address(
    val id: String,
    val name: String,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val phone: String,
    val isDefault: Boolean = false
)
