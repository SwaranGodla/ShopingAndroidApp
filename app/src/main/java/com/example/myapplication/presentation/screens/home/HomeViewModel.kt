package com.example.myapplication.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.usecase.product.GetCategoriesUseCase
import com.example.myapplication.domain.usecase.product.GetProductsUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the home screen.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    private val _featuredProducts = MutableStateFlow<List<Product>>(emptyList())
    val featuredProducts: StateFlow<List<Product>> = _featuredProducts
    
    private val _newArrivals = MutableStateFlow<List<Product>>(emptyList())
    val newArrivals: StateFlow<List<Product>> = _newArrivals
    
    private val _popularProducts = MutableStateFlow<List<Product>>(emptyList())
    val popularProducts: StateFlow<List<Product>> = _popularProducts
    
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories
    
    private var searchQuery = ""
    
    init {
        loadHomeData()
    }
    
    /**
     * Load all data needed for the home screen.
     */
    fun loadHomeData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            // Load featured products
            when (val result = getProductsUseCase(forceRefresh = true, limit = 5)) {
                is Resource.Success -> {
                    _featuredProducts.value = result.data
                }
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
            }
            
            // Load new arrivals
            when (val result = getProductsUseCase(forceRefresh = true, limit = 10)) {
                is Resource.Success -> {
                    _newArrivals.value = result.data
                }
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
            }
            
            // Load popular products
            when (val result = getProductsUseCase(forceRefresh = true, limit = 10)) {
                is Resource.Success -> {
                    _popularProducts.value = result.data
                }
                is Resource.Error -> {
                    errorMessage = result.message
                }
                else -> {}
            }
            
            // Load categories
            when (val result = getCategoriesUseCase()) {
                is Resource.Success -> {
                    _categories.value = result.data
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
     * Search for products with the given query.
     */
    fun searchProducts(query: String) {
        searchQuery = query
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = getProductsUseCase(forceRefresh = true, searchQuery = query)) {
                is Resource.Success -> {
                    _featuredProducts.value = result.data
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
}
