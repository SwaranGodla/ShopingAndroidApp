package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.usecase.cart.AddToCartUseCase
import com.example.myapplication.domain.usecase.product.GetFavoriteProductsUseCase
import com.example.myapplication.domain.usecase.product.ToggleFavoriteProductUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the wishlist screen.
 */
@HiltViewModel
class WishListViewModel @Inject constructor(
    getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val toggleFavoriteProductUseCase: ToggleFavoriteProductUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    // Wishlist items as a StateFlow
    val wishlistItems: StateFlow<List<Product>> = getFavoriteProductsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Events
    private val _wishlistEvent = MutableSharedFlow<WishlistEvent>()
    val wishlistEvent: SharedFlow<WishlistEvent> = _wishlistEvent
    
    /**
     * Remove an item from the wishlist.
     */
    fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = toggleFavoriteProductUseCase(productId, false)) {
                is Resource.Success -> {
                    _wishlistEvent.emit(WishlistEvent.RemovedFromWishlist)
                }
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
            }
            
            isLoading = false
        }
    }
    
    /**
     * Add a product to the cart.
     */
    fun addToCart(productId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = addToCartUseCase(productId)) {
                is Resource.Success -> {
                    _wishlistEvent.emit(WishlistEvent.AddedToCart)
                }
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
    
    /**
     * Events emitted by the WishListViewModel.
     */
    sealed class WishlistEvent {
        object RemovedFromWishlist : WishlistEvent()
        object AddedToCart : WishlistEvent()
    }
}
