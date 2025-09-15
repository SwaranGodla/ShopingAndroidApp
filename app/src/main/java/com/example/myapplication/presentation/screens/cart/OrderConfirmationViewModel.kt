package com.example.myapplication.presentation.screens.cart

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderStatus
import com.example.myapplication.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the order confirmation screen.
 */
@HiltViewModel
class OrderConfirmationViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get the order ID from the saved state handle
    private val orderId: String = checkNotNull(savedStateHandle["orderId"])

    // UI state
    private val _uiState = MutableStateFlow<OrderConfirmationUiState>(OrderConfirmationUiState.Loading)
    val uiState: StateFlow<OrderConfirmationUiState> = _uiState.asStateFlow()

    init {
        loadOrderDetails()
    }

    /**
     * Load the order details from the repository.
     */
    private fun loadOrderDetails() {
        viewModelScope.launch {
            _uiState.value = OrderConfirmationUiState.Loading
            
            try {
                val order = orderRepository.getOrderById(orderId)
                if (order != null) {
                    _uiState.value = OrderConfirmationUiState.Success(order)
                } else {
                    _uiState.value = OrderConfirmationUiState.Error("Order not found")
                }
            } catch (e: Exception) {
                _uiState.value = OrderConfirmationUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    /**
     * Format a date as a string.
     */
    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        return formatter.format(date)
    }
}

/**
 * UI state for the order confirmation screen.
 */
sealed class OrderConfirmationUiState {
    object Loading : OrderConfirmationUiState()
    data class Success(val order: Order) : OrderConfirmationUiState()
    data class Error(val message: String) : OrderConfirmationUiState()
}
