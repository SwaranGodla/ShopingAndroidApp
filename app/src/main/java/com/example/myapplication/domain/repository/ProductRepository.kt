package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for product-related operations.
 */
interface ProductRepository {
    
    /**
     * Get a list of products with optional filtering.
     *
     * @param forceRefresh Whether to force a refresh from the remote API.
     * @param category Optional category filter.
     * @param searchQuery Optional search query.
     * @param page Page number for pagination.
     * @param limit Number of items per page.
     * @return Resource containing a list of products or an error.
     */
    suspend fun getProducts(
        forceRefresh: Boolean = false,
        category: String? = null,
        searchQuery: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): Resource<List<Product>>
    
    /**
     * Get detailed information about a specific product.
     *
     * @param productId The ID of the product.
     * @param forceRefresh Whether to force a refresh from the remote API.
     * @return Resource containing product details or an error.
     */
    suspend fun getProductDetails(
        productId: String,
        forceRefresh: Boolean = false
    ): Resource<ProductDetailsResponse>
    
    /**
     * Get a list of all product categories.
     *
     * @return Resource containing a list of category names or an error.
     */
    suspend fun getCategories(): Resource<List<String>>
    
    /**
     * Get a flow of favorite products.
     *
     * @return Flow of favorite products list.
     */
    fun getFavoriteProducts(): Flow<List<Product>>
    
    /**
     * Update the favorite status of a product.
     *
     * @param productId The ID of the product.
     * @param isFavorite Whether the product should be marked as favorite.
     * @return Resource indicating success or failure.
     */
    suspend fun updateFavoriteStatus(productId: String, isFavorite: Boolean): Resource<Unit>
}
