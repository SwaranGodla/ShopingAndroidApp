package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for retrieving a list of products with optional filtering.
 */
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
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
    suspend operator fun invoke(
        forceRefresh: Boolean = false,
        category: String? = null,
        searchQuery: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): Resource<List<Product>> {
        return productRepository.getProducts(
            forceRefresh = forceRefresh,
            category = category,
            searchQuery = searchQuery,
            page = page,
            limit = limit
        )
    }
}
