package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.ProductDetailsResponse
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.util.Resource
import javax.inject.Inject

/**
 * Use case for retrieving detailed information about a specific product.
 */
class GetProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    /**
     * Get detailed information about a specific product.
     *
     * @param productId The ID of the product.
     * @param forceRefresh Whether to force a refresh from the remote API.
     * @return Resource containing product details or an error.
     */
    suspend operator fun invoke(
        productId: String,
        forceRefresh: Boolean = false
    ): Resource<ProductDetailsResponse> {
        if (productId.isBlank()) {
            return Resource.Error("Product ID cannot be empty")
        }
        
        return productRepository.getProductDetails(productId, forceRefresh)
    }
}
