package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.ProductDao
import com.example.myapplication.data.local.entity.toProduct
import com.example.myapplication.data.local.entity.toProductEntity
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.data.remote.ShoppingApiService
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of the ProductRepository interface.
 * Handles data operations for products, including fetching from API and caching in local database.
 */
class ProductRepositoryImpl @Inject constructor(
    private val api: ShoppingApiService,
    private val productDao: ProductDao
) : ProductRepository {
    
    override suspend fun getProducts(
        forceRefresh: Boolean,
        category: String?,
        searchQuery: String?,
        page: Int,
        limit: Int
    ): Resource<List<Product>> {
        return try {
            if (forceRefresh) {
                val remoteProducts = api.getProducts(
                    category = category,
                    searchQuery = searchQuery,
                    page = page,
                    limit = limit
                )
                
                // Cache products in local database
                productDao.insertProducts(remoteProducts.map { it.toProductEntity() })
                
                Resource.Success(remoteProducts as List<Product>)
            } else {
                // Return cached products
                val localProducts = if (category != null) {
                    productDao.getProductsByCategory(category).first() // Collect the first emission from the Flow
                } else if (searchQuery != null) {
                    productDao.searchProducts(searchQuery).first() // Collect the first emission from the Flow
                } else {
                    productDao.getAllProducts().first() // Collect the first emission from the Flow
                }
                
                // Map ProductEntity objects to Product domain models
                val products = localProducts.map { it.toProduct() }
                Resource.Success(products)
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching products")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun getProductDetails(productId: String, forceRefresh: Boolean): Resource<ProductDetailsResponse> {
        return try {
            if (forceRefresh) {
                val productDetails = api.getProductDetails(productId)
                
                // Cache the main product and related products
                productDao.insertProduct(productDetails.product.toProductEntity())
                productDao.insertProducts(productDetails.relatedProducts.map { it.toProductEntity() })
                
                Resource.Success(productDetails)
            } else {
                // Try to get from local database first
                val product = productDao.getProductById(productId)
                
                if (product != null) {
                    // We have the main product, but we don't have related products or reviews in local DB
                    // So we'll need to fetch from API anyway for complete details
                    Resource.Success(api.getProductDetails(productId))
                } else {
                    // Not in local database, fetch from API
                    val productDetails = api.getProductDetails(productId)
                    
                    // Cache the main product and related products
                    productDao.insertProduct(productDetails.product.toProductEntity())
                    productDao.insertProducts(productDetails.relatedProducts.map { it.toProductEntity() })
                    
                    Resource.Success(productDetails)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching product details")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override suspend fun getCategories(): Resource<List<String>> {
        return try {
            val categories = api.getCategories()
            Resource.Success(categories)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching categories")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
    
    override fun getFavoriteProducts(): Flow<List<Product>> {
        return productDao.getFavoriteProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }
    
    override suspend fun updateFavoriteStatus(productId: String, isFavorite: Boolean): Resource<Unit> {
        return try {
            productDao.updateFavoriteStatus(productId, isFavorite)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating favorite status")
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}
