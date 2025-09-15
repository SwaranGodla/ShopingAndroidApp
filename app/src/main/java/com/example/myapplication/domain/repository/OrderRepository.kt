package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.Order
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for order-related operations.
 */
interface OrderRepository {

    /**
     * Get a list of orders for the current user.
     * 
     * @param forceRefresh Whether to force a refresh from the remote API.
     * @return Resource containing a list of orders or an error.
     */
    suspend fun getOrders(forceRefresh: Boolean = false): Resource<List<Order>>
    
    /**
     * Get an order by its ID.
     * 
     * @param orderId The ID of the order to retrieve.
     * @return The order if found, null otherwise.
     */
    suspend fun getOrderById(orderId: String): Order?
    
    /**
     * Get a flow of orders for the current user.
     * 
     * @return Flow of orders list.
     */
    fun getOrdersFlow(): Flow<List<Order>>
    
    /**
     * Create a new order.
     * 
     * @param order The order to create.
     * @return Resource indicating success with the created order ID, or an error.
     */
    suspend fun createOrder(order: Order): Resource<String>
    
    /**
     * Update the status of an order.
     * 
     * @param orderId The ID of the order to update.
     * @param status The new status for the order.
     * @return Resource indicating success or failure.
     */
    suspend fun updateOrderStatus(orderId: String, status: com.example.myapplication.data.model.OrderStatus): Resource<Unit>
    
    /**
     * Cancel an order.
     * 
     * @param orderId The ID of the order to cancel.
     * @param reason Optional reason for cancellation.
     * @return Resource indicating success or failure.
     */
    suspend fun cancelOrder(orderId: String, reason: String? = null): Resource<Unit>
}
