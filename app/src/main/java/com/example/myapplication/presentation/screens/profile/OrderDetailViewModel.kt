package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
 * Data class representing shipping address information.
 */
data class ShippingAddress(
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
)

/**
 * Data class representing payment information.
 */
data class PaymentInfo(
    val method: String,
    val cardLast4: String? = null,
    val cardType: String? = null
)

/**
 * Data class representing order tracking information.
 */
data class TrackingInfo(
    val carrier: String,
    val trackingNumber: String,
    val estimatedDelivery: String,
    val trackingEvents: List<TrackingEvent>
)

/**
 * Data class representing a tracking event.
 */
data class TrackingEvent(
    val date: String,
    val time: String,
    val location: String,
    val status: String
)

/**
 * ViewModel for the order detail screen.
 */
@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Extract order ID from navigation arguments
    private val orderId: String = checkNotNull(savedStateHandle["orderId"])
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var order by mutableStateOf<Order?>(null)
        private set
    
    var shippingAddress by mutableStateOf<ShippingAddress?>(null)
        private set
    
    var paymentInfo by mutableStateOf<PaymentInfo?>(null)
        private set
    
    var trackingInfo by mutableStateOf<TrackingInfo?>(null)
        private set
    
    init {
        loadOrderDetails()
    }
    
    /**
     * Load the details of the current order.
     */
    fun loadOrderDetails() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                // In a real app, we would call a use case to get the order details
                // For this example, we'll generate some mock data
                delay(1000) // Simulate network delay
                
                val mockOrder = generateMockOrder()
                order = mockOrder
                shippingAddress = generateMockShippingAddress()
                paymentInfo = generateMockPaymentInfo()
                
                // Only add tracking info if the order is shipped or delivered
                if (mockOrder.status == "Shipped" || mockOrder.status == "Delivered") {
                    trackingInfo = generateMockTrackingInfo(mockOrder.status)
                }
                
            } catch (e: Exception) {
                errorMessage = "Failed to load order details: ${e.message}"
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
     * Generate a mock order for demonstration purposes.
     */
    private fun generateMockOrder(): Order {
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
            )
        )
        
        return Order(
            id = orderId,
            date = dateFormat.format(Date(currentDate.time - 2 * 24 * 60 * 60 * 1000)), // 2 days ago
            status = "Delivered",
            totalAmount = mockItems.sumOf { it.price * it.quantity },
            items = mockItems
        )
    }
    
    /**
     * Generate mock shipping address for demonstration purposes.
     */
    private fun generateMockShippingAddress(): ShippingAddress {
        return ShippingAddress(
            name = "John Doe",
            address = "123 Main Street, Apt 4B",
            city = "San Francisco",
            state = "CA",
            zipCode = "94105",
            country = "United States"
        )
    }
    
    /**
     * Generate mock payment information for demonstration purposes.
     */
    private fun generateMockPaymentInfo(): PaymentInfo {
        return PaymentInfo(
            method = "Credit Card",
            cardLast4 = "4242",
            cardType = "Visa"
        )
    }
    
    /**
     * Generate mock tracking information for demonstration purposes.
     */
    private fun generateMockTrackingInfo(orderStatus: String): TrackingInfo {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val currentDate = Date()
        
        val events = mutableListOf<TrackingEvent>()
        
        // Order placed
        events.add(
            TrackingEvent(
                date = dateFormat.format(Date(currentDate.time - 5 * 24 * 60 * 60 * 1000)),
                time = "09:15 AM",
                location = "Online",
                status = "Order Placed"
            )
        )
        
        // Processing
        events.add(
            TrackingEvent(
                date = dateFormat.format(Date(currentDate.time - 4 * 24 * 60 * 60 * 1000)),
                time = "02:30 PM",
                location = "Warehouse",
                status = "Processing"
            )
        )
        
        // Shipped
        events.add(
            TrackingEvent(
                date = dateFormat.format(Date(currentDate.time - 3 * 24 * 60 * 60 * 1000)),
                time = "10:45 AM",
                location = "Distribution Center",
                status = "Shipped"
            )
        )
        
        // Out for delivery
        if (orderStatus == "Delivered") {
            events.add(
                TrackingEvent(
                    date = dateFormat.format(Date(currentDate.time - 1 * 24 * 60 * 60 * 1000)),
                    time = "08:20 AM",
                    location = "Local Facility",
                    status = "Out for Delivery"
                )
            )
            
            // Delivered
            events.add(
                TrackingEvent(
                    date = dateFormat.format(Date(currentDate.time - 1 * 24 * 60 * 60 * 1000)),
                    time = "02:15 PM",
                    location = "Destination",
                    status = "Delivered"
                )
            )
        }
        
        return TrackingInfo(
            carrier = "Express Shipping",
            trackingNumber = "ES${(1000000..9999999).random()}",
            estimatedDelivery = if (orderStatus == "Delivered") {
                "Delivered"
            } else {
                dateFormat.format(Date(currentDate.time + 1 * 24 * 60 * 60 * 1000))
            },
            trackingEvents = events
        )
    }
}
