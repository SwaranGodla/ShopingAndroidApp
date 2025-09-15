package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

/**
 * Data class representing an order in the order history.
 */
data class Order(
    val id: String,
    val date: String,
    val status: String,
    val totalAmount: Double,
    val items: List<OrderItem>
)

/**
 * Data class representing an item in an order.
 */
data class OrderItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String
)

/**
 * ViewModel for the order history screen.
 */
@HiltViewModel
class OrderHistoryViewModel @Inject constructor() : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var orders by mutableStateOf<List<Order>>(emptyList())
        private set
    
    init {
        loadOrderHistory()
    }
    
    /**
     * Load the user's order history.
     */
    fun loadOrderHistory() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // In a real app, we would call a use case to get the order history
                // For this example, we'll generate some mock data
                delay(1000) // Simulate network delay
                
                orders = generateMockOrders()
                
            } catch (e: Exception) {
                errorMessage = "Failed to load order history: ${e.message}"
            }
            
            isLoading = false
        }
    }
    
    /**
     * Clear any error messages.
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Generate mock order data for demonstration purposes.
     */
    private fun generateMockOrders(): List<Order> {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val currentDate = Date()
        
        val mockItems = listOf(
            OrderItem(
                id = UUID.randomUUID().toString(),
                name = "Smartphone X Pro",
                price = 899.99,
                quantity = 1,
                imageUrl = "https://via.placeholder.com/150"
            ),
            OrderItem(
                id = UUID.randomUUID().toString(),
                name = "Wireless Earbuds",
                price = 129.99,
                quantity = 1,
                imageUrl = "https://via.placeholder.com/150"
            ),
            OrderItem(
                id = UUID.randomUUID().toString(),
                name = "Smart Watch",
                price = 249.99,
                quantity = 1,
                imageUrl = "https://via.placeholder.com/150"
            ),
            OrderItem(
                id = UUID.randomUUID().toString(),
                name = "Laptop Pro",
                price = 1299.99,
                quantity = 1,
                imageUrl = "https://via.placeholder.com/150"
            ),
            OrderItem(
                id = UUID.randomUUID().toString(),
                name = "Bluetooth Speaker",
                price = 79.99,
                quantity = 2,
                imageUrl = "https://via.placeholder.com/150"
            )
        )
        
        return listOf(
            Order(
                id = "ORD-123456",
                date = dateFormat.format(Date(currentDate.time - 2 * 24 * 60 * 60 * 1000)), // 2 days ago
                status = "Delivered",
                totalAmount = mockItems[0].price + mockItems[1].price,
                items = listOf(mockItems[0], mockItems[1])
            ),
            Order(
                id = "ORD-123457",
                date = dateFormat.format(Date(currentDate.time - 7 * 24 * 60 * 60 * 1000)), // 7 days ago
                status = "Shipped",
                totalAmount = mockItems[2].price,
                items = listOf(mockItems[2])
            ),
            Order(
                id = "ORD-123458",
                date = dateFormat.format(Date(currentDate.time - 14 * 24 * 60 * 60 * 1000)), // 14 days ago
                status = "Delivered",
                totalAmount = mockItems[3].price + mockItems[4].price * 2,
                items = listOf(mockItems[3], mockItems[4].copy(quantity = 2))
            )
        )
    }
}
