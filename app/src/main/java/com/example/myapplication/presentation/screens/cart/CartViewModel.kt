package com.example.myapplication.presentation.screens.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.usecase.cart.GetCartItemsUseCase
import com.example.myapplication.domain.usecase.cart.GetCartStatsUseCase
import com.example.myapplication.domain.usecase.cart.RemoveFromCartUseCase
import com.example.myapplication.domain.usecase.cart.UpdateCartItemUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the cart screen.
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    getCartItemsUseCase: GetCartItemsUseCase,
    getCartStatsUseCase: GetCartStatsUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase
) : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    // Cart items as a StateFlow
    val cartItems: StateFlow<List<Product>> = getCartItemsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Cart total price as a StateFlow
    val cartTotal: StateFlow<Double> = getCartStatsUseCase.getCartTotal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
    
    // Cart item count as a StateFlow
    val cartItemCount: StateFlow<Int> = getCartStatsUseCase.getCartItemCount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    
    /**
     * Update the quantity of a product in the cart.
     */
    fun updateCartItemQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = updateCartItemUseCase(productId, quantity)) {
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
            }
            
            isLoading = false
        }
    }
    
    /**
     * Remove a product from the cart.
     */
    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = removeFromCartUseCase(productId)) {
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
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
}
