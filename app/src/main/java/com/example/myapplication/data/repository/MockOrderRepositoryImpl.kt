package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderStatus
import com.example.myapplication.data.util.FakeDataProvider
import com.example.myapplication.domain.repository.OrderRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock implementation of OrderRepository that uses fake data for testing and development.
 */
@Singleton
class MockOrderRepositoryImpl @Inject constructor() : OrderRepository {
    
    // In-memory cache of orders
    private val ordersCache = MutableStateFlow(FakeDataProvider.generateOrders(10))
    
    override suspend fun getOrders(forceRefresh: Boolean): Resource<List<Order>> {
        // Simulate network delay
        delay(1000)
        
        return Resource.Success(ordersCache.value)
    }
    
    override suspend fun getOrderById(orderId: String): Order? {
        // Simulate network delay
        delay(500)
        
        // Find and return the order or null if not found
        return ordersCache.value.find { it.id == orderId }
    }
    
    override suspend fun createOrder(order: Order): Resource<String> {
        // Simulate network delay
        delay(1500)
        
        // Add to cache
        ordersCache.update { currentOrders ->
            currentOrders + order
        }
        
        return Resource.Success(order.id)
    }
    
    override fun getOrdersFlow(): Flow<List<Order>> {
        return ordersCache
    }
    
    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Resource<Unit> {
        // Simulate network delay
        delay(1000)
        
        val orderIndex = ordersCache.value.indexOfFirst { it.id == orderId }
        if (orderIndex >= 0) {
            val updatedOrder = ordersCache.value[orderIndex].copy(
                status = status,
                updatedAt = java.util.Date().toString()
            )
            
            // Update cache
            ordersCache.update { currentOrders ->
                currentOrders.toMutableList().apply {
                    this[orderIndex] = updatedOrder
                }
            }
            
            return Resource.Success(Unit)
        } else {
            return Resource.Error("Order not found")
        }
    }
    
    override suspend fun cancelOrder(orderId: String, reason: String?): Resource<Unit> {
        // Simulate network delay
        delay(1000)
        
        val orderIndex = ordersCache.value.indexOfFirst { it.id == orderId }
        if (orderIndex >= 0) {
            val updatedOrder = ordersCache.value[orderIndex].copy(
                status = OrderStatus.CANCELLED,
                updatedAt = java.util.Date().toString()
            )
            
            // Update cache
            ordersCache.update { currentOrders ->
                currentOrders.toMutableList().apply {
                    this[orderIndex] = updatedOrder
                }
            }
            
            return Resource.Success(Unit)
        } else {
            return Resource.Error("Order not found")
        }
    }
}
