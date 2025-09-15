package com.example.myapplication.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.usecase.product.GetProductsUseCase
import com.example.myapplication.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the category products screen.
 */
@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Extract category name from navigation arguments
    private val categoryName: String = checkNotNull(savedStateHandle["categoryName"])
    
    // UI state
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var products by mutableStateOf<List<Product>>(emptyList())
        private set
    
    var sortOption by mutableStateOf(SortOption.POPULARITY)
        private set
    
    var filterOption by mutableStateOf<FilterOption?>(null)
        private set
    
    init {
        loadCategoryProducts()
    }
    
    /**
     * Load products for the current category.
     */
    fun loadCategoryProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            when (val result = getProductsUseCase(
                forceRefresh = true,
                category = categoryName
            )) {
                is Resource.Success -> {
                    products = result.data
                    applySortAndFilter()
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
     * Update the sort option and apply it.
     */
    fun updateSortOption(option: SortOption) {
        sortOption = option
        applySortAndFilter()
    }
    
    /**
     * Update the filter option and apply it.
     */
    fun updateFilterOption(option: FilterOption?) {
        filterOption = option
        applySortAndFilter()
    }
    
    /**
     * Apply the current sort and filter options to the products list.
     */
    private fun applySortAndFilter() {
        var filteredProducts = products
        
        // Apply filter
        filterOption?.let { filter ->
            filteredProducts = when (filter) {
                is FilterOption.PriceRange -> {
                    filteredProducts.filter { product ->
                        product.finalPrice >= filter.min && product.finalPrice <= filter.max
                    }
                }
                is FilterOption.Rating -> {
                    filteredProducts.filter { product ->
                        product.rating >= filter.minRating
                    }
                }
            }
        }
        
        // Apply sort
        filteredProducts = when (sortOption) {
            SortOption.PRICE_LOW_TO_HIGH -> filteredProducts.sortedBy { it.finalPrice }
            SortOption.PRICE_HIGH_TO_LOW -> filteredProducts.sortedByDescending { it.finalPrice }
            SortOption.RATING -> filteredProducts.sortedByDescending { it.rating }
            SortOption.POPULARITY -> filteredProducts // Assume default order is by popularity
        }
        
        products = filteredProducts
    }
    
    /**
     * Clear any error messages.
     */
    fun clearError() {
        errorMessage = null
    }
    
    /**
     * Sort options for products.
     */
    enum class SortOption {
        POPULARITY,
        PRICE_LOW_TO_HIGH,
        PRICE_HIGH_TO_LOW,
        RATING
    }
    
    /**
     * Filter options for products.
     */
    sealed class FilterOption {
        data class PriceRange(val min: Double, val max: Double) : FilterOption()
        data class Rating(val minRating: Float) : FilterOption()
    }
}
