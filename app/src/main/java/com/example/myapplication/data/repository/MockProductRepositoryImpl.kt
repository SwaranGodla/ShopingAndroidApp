package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductCategory
import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.data.util.FakeDataProvider
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock implementation of ProductRepository that uses fake data for testing and development.
 */
@Singleton
class MockProductRepositoryImpl @Inject constructor() : ProductRepository {
    
    // In-memory cache of products
    private val products = MutableStateFlow(FakeDataProvider.generateProducts(20))
    
    // In-memory cache of favorite products
    private val favoriteProducts = MutableStateFlow<Set<String>>(emptySet())
    
    override suspend fun getProducts(
        forceRefresh: Boolean,
        category: String?,
        searchQuery: String?,
        page: Int,
        limit: Int
    ): Resource<List<Product>> {
        // Simulate network delay
        delay(1000)
        
        var filteredProducts = products.value
        
        // Apply category filter if provided
        if (category != null) {
            filteredProducts = filteredProducts.filter { it.category.equals(category, ignoreCase = true) }
        }
        
        // Apply search query if provided
        if (searchQuery != null) {
            filteredProducts = filteredProducts.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true) ||
                it.brand.contains(searchQuery, ignoreCase = true)
            }
        }
        
        // Apply pagination
        val startIndex = (page - 1) * limit
        val endIndex = minOf(startIndex + limit, filteredProducts.size)
        
        // Return paginated results
        return if (startIndex < filteredProducts.size) {
            Resource.Success(filteredProducts.subList(startIndex, endIndex))
        } else {
            Resource.Success(emptyList())
        }
    }
    
    // This method is not in the ProductRepository interface, so we're removing it
    
    override suspend fun getProductDetails(
        productId: String,
        forceRefresh: Boolean
    ): Resource<ProductDetailsResponse> {
        // Simulate network delay
        delay(1000)
        
        val product = products.value.find { it.id == productId }
        return if (product != null) {
            val details = FakeDataProvider.generateProductDetails(productId)
            Resource.Success(details)
        } else {
            Resource.Error("Product not found")
        }
    }
    
    // This method is not in the ProductRepository interface, so we're removing it
    
    override suspend fun getCategories(): Resource<List<String>> {
        // Simulate network delay
        delay(1000)
        
        // Extract category names from the fake categories
        val categoryNames = FakeDataProvider.generateProductCategories().map { it.name }
        return Resource.Success(categoryNames)
    }
    
    override suspend fun updateFavoriteStatus(productId: String, isFavorite: Boolean): Resource<Unit> {
        // Simulate network delay
        delay(500)
        
        val currentFavorites = favoriteProducts.value.toMutableSet()
        
        if (isFavorite) {
            currentFavorites.add(productId)
        } else {
            currentFavorites.remove(productId)
        }
        
        favoriteProducts.value = currentFavorites
        
        return Resource.Success(Unit)
    }
    
    override fun getFavoriteProducts(): Flow<List<Product>> {
        return favoriteProducts.map { favoriteIds ->
            products.value.filter { favoriteIds.contains(it.id) }
        }
    }
    
    // This method is not in the ProductRepository interface, so we're removing it
}
