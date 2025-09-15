package com.example.myapplication.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.data.model.ProductReview
import com.example.myapplication.domain.usecase.cart.AddToCartUseCase
import com.example.myapplication.domain.usecase.product.GetProductDetailsUseCase
import com.example.myapplication.domain.usecase.product.ToggleFavoriteProductUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the product detail screen.
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val toggleFavoriteProductUseCase: ToggleFavoriteProductUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Extract product ID from navigation arguments
    private val productId: String = checkNotNull(savedStateHandle["productId"])
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var product by mutableStateOf<Product?>(null)
        private set
    
    var relatedProducts by mutableStateOf<List<Product>>(emptyList())
        private set
    
    var reviews by mutableStateOf<List<ProductReview>>(emptyList())
        private set
    
    var isFavorite by mutableStateOf(false)
        private set
    
    var selectedQuantity by mutableStateOf(1)
        private set
    
    var selectedImageIndex by mutableStateOf(0)
        private set
    
    // Events
    private val _productDetailEvent = MutableSharedFlow<ProductDetailEvent>()
    val productDetailEvent: SharedFlow<ProductDetailEvent> = _productDetailEvent
    
    init {
        loadProductDetails()
    }
    
    /**
     * Load product details for the current product ID.
     */
    fun loadProductDetails() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = getProductDetailsUseCase(productId)) {
                is Resource.Success -> {
                    val productDetails = result.data
                    product = productDetails.product
                    relatedProducts = productDetails.relatedProducts
                    reviews = productDetails.reviews
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
     * Update the selected quantity.
     */
    fun updateQuantity(quantity: Int) {
        if (quantity > 0) {
            selectedQuantity = quantity
        }
    }
    
    /**
     * Increase the selected quantity by 1.
     */
    fun increaseQuantity() {
        selectedQuantity++
    }
    
    /**
     * Decrease the selected quantity by 1, but not below 1.
     */
    fun decreaseQuantity() {
        if (selectedQuantity > 1) {
            selectedQuantity--
        }
    }
    
    /**
     * Update the selected image index.
     */
    fun updateSelectedImageIndex(index: Int) {
        selectedImageIndex = index
    }
    
    /**
     * Toggle the favorite status of the current product.
     */
    fun toggleFavorite() {
        viewModelScope.launch {
            product?.let { currentProduct ->
                isFavorite = !isFavorite
                
                when (val result = toggleFavoriteProductUseCase(currentProduct.id, isFavorite)) {
                    is Resource.Error -> {
                        // Revert the UI state if the operation failed
                        isFavorite = !isFavorite
                        errorMessage = result.message
                    }
                    else -> {}
                }
            }
        }
    }
    
    /**
     * Add the current product to the cart with the selected quantity.
     */
    fun addToCart() {
        viewModelScope.launch {
            product?.let { currentProduct ->
                isLoading = true
                errorMessage = null
                
                when (val result = addToCartUseCase(currentProduct.id, selectedQuantity)) {
                    is Resource.Success -> {
                        if (result.data) {
                            _productDetailEvent.emit(ProductDetailEvent.AddedToCart)
                        } else {
                            errorMessage = "Failed to add product to cart"
                        }
                    }
                    is Resource.Error -> {
                        errorMessage = result.message
                    }
                    else -> {}
                }
                
                isLoading = false
            }
        }
    }
    
    /**
     * Clear any error messages.
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Events emitted by the ProductDetailViewModel.
     */
    sealed class ProductDetailEvent {
        object AddedToCart : ProductDetailEvent()
    }
}
