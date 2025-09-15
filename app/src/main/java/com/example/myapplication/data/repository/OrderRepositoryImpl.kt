package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderStatus
import com.example.myapplication.data.remote.ShoppingApiService
import com.example.myapplication.domain.repository.OrderRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the OrderRepository interface.
 * Handles data operations for orders, including fetching from API and caching in memory.
 */
@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val api: ShoppingApiService
) : OrderRepository {
    
    // In-memory cache of orders
    private val ordersCache = mutableListOf<Order>()
    private val _ordersFlow = MutableStateFlow<List<Order>>(emptyList())
    
    override suspend fun getOrders(forceRefresh: Boolean): Resource<List<Order>> {
        return try {
            if (forceRefresh || ordersCache.isEmpty()) {
                // In a real app, this would call the API
                // val remoteOrders = api.getOrders()
                // ordersCache.clear()
                // ordersCache.addAll(remoteOrders)
                
                // For now, return the cached orders or mock data if empty
                if (ordersCache.isEmpty()) {
                    // This would be replaced with actual API call in production
                    Timber.d("No orders in cache and no API implementation yet")
                }
            }
            
            _ordersFlow.value = ordersCache.toList()
            Resource.Success(ordersCache.toList())
        } catch (e: Exception) {
            Timber.e(e, "Error fetching orders")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun getOrderById(orderId: String): Order? {
        return try {
            // First check the cache
            ordersCache.find { it.id == orderId } ?: run {
                // In a real app, this would call the API if not found in cache
                // val order = api.getOrderById(orderId)
                // if (order != null) {
                //     ordersCache.add(order)
                // }
                // return order
                
                // For now, return null if not in cache
                Timber.d("Order not found in cache and no API implementation yet")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching order details")
            null
        }
    }
    
    override fun getOrdersFlow(): Flow<List<Order>> {
        return _ordersFlow.asStateFlow()
    }
    
    override suspend fun createOrder(order: Order): Resource<String> {
        return try {
            // In a real app, this would call the API
            // val orderId = api.createOrder(order)
            
            // For now, just add to the cache
            ordersCache.add(order)
            _ordersFlow.value = ordersCache.toList()
            
            Resource.Success(order.id)
        } catch (e: Exception) {
            Timber.e(e, "Error creating order")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Resource<Unit> {
        return try {
            // In a real app, this would call the API
            // api.updateOrderStatus(orderId, status)
            
            // For now, update in the cache
            val orderIndex = ordersCache.indexOfFirst { it.id == orderId }
            if (orderIndex != -1) {
                val updatedOrder = ordersCache[orderIndex].copy(status = status)
                ordersCache[orderIndex] = updatedOrder
                _ordersFlow.value = ordersCache.toList()
                Resource.Success(Unit)
            } else {
                Resource.Error("Order not found")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating order status")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun cancelOrder(orderId: String, reason: String?): Resource<Unit> {
        return try {
            // In a real app, this would call the API
            // api.cancelOrder(orderId, reason)
            
            // For now, update in the cache
            val orderIndex = ordersCache.indexOfFirst { it.id == orderId }
            if (orderIndex != -1) {
                val updatedOrder = ordersCache[orderIndex].copy(status = OrderStatus.CANCELLED)
                ordersCache[orderIndex] = updatedOrder
                _ordersFlow.value = ordersCache.toList()
                Resource.Success(Unit)
            } else {
                Resource.Error("Order not found")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error cancelling order")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}
